package com.HomeStudio.QualityFPV

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Connection
import org.jsoup.Jsoup

class LoginDialogFragment(val prodUrl: String): DialogFragment() {

    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mSiteSelectorViewModel = ViewModelProvider(activity as MainActivity).get(SiteSelectorViewModel::class.java)
        val root = inflater.inflate(R.layout.dialog_fragment_login, container, false)
        val webView = root.findViewById<WebView>(R.id.webpage)

        when(mSiteSelectorViewModel.website.value){
            "GetFpv" -> {
                webView.apply {
                    webViewClient = WebViewClient()
                    loadUrl("https://www.getfpv.com/customer/account/login/")
                    settings.javaScriptEnabled = true
                }
            }

            "Pyro Drone" -> {
                webView.apply {
                    webViewClient = WebViewClient()
                    loadUrl("https://pyrodrone.com/account/login")
                    settings.javaScriptEnabled = true
                }
            }

            "RaceDayQuads" -> {
                webView.apply {
                    webViewClient = WebViewClient()
                    loadUrl("https://www.racedayquads.com/account/login")
                    settings.javaScriptEnabled = true
                }
            }
        }



        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {

                var cookies = getCookieMap(url!!)

                when(mSiteSelectorViewModel.website.value){
                    "GetFpv" ->
                    {
                        doAsync {

                            if(url == "https://www.getfpv.com/customer/account/"){

                                val page = Jsoup.connect(prodUrl)
                                    .method(Connection.Method.GET)
                                    .cookies(cookies)
                                    .execute()

                                cookies = page.cookies()

                                val parsed = page.parse()

                                val productDataMap: HashMap<String, String> = hashMapOf()
                                productDataMap["form_key"] = parsed.getElementsByClass("product-essential")[0].getElementsByTag("input")[0].attr("value").toString()
                                productDataMap["product"] = parsed.getElementsByClass("product-essential")[0].getElementsByTag("input")[1].attr("value").toString()
                                productDataMap["qty"] = "1"

                                val add = Jsoup.connect(parsed.getElementsByClass("product-essential")[0].getElementsByTag("form").attr("action").toString())
                                    .data(productDataMap)
                                    .cookies(cookies)
                                    .method(Connection.Method.POST)
                                    .followRedirects(true)
                                    .execute()

                                dismiss()
                            }
                        }
                    }


                    "Pyro Drone" ->
                    {
                        doAsync {

                            if(url == "https://pyrodrone.com/account") {

                                Log.d("out", "adding pyro")


                                val page = Jsoup.connect(prodUrl)
                                    .method(Connection.Method.GET)
                                    .cookies(cookies)
                                    .execute()

                                cookies = page.cookies()

                                val productDataMap: HashMap<String, String> = hashMapOf()
                                productDataMap["id"] = page.parse().getElementsByTag("option").attr("value").toString()
                                productDataMap["quantity"] = "1"

                                val add = Jsoup.connect("https://pyrodrone.com/cart/add.js")
                                    .data(productDataMap)
                                    .cookies(cookies)
                                    .method(Connection.Method.POST)
                                    .followRedirects(true)
                                    .execute()

                                Log.d("out", "added pyro " + add.statusCode().toString())

                                dismiss()
                            }
                        }
                    }

                    "RaceDayQuads" ->
                    {
                        doAsync {
                            if(url == "https://www.racedayquads.com/account"){

                                val page = Jsoup.connect(prodUrl)
                                    .method(Connection.Method.GET)
                                    .cookies(cookies)
                                    .execute()

                                cookies = page.cookies()

                                val parsed = page.parse()

                                Log.d("out",  parsed.getElementsByClass("product-main")[0].getElementsByTag("input")[2].attr("value").toString())

                                val productDataMap: HashMap<String, String> = hashMapOf()
                                productDataMap["form_type"] = "product"
                                productDataMap["utf8"] = "e2 9c 93"
                                productDataMap["id"] = parsed.getElementsByClass("product-main")[0].getElementsByTag("input")[2].attr("value").toString()
                                productDataMap["quantity"] = "1"


                                Log.d("out", "rdq " + parsed.getElementsByClass("product-main")[0].getElementsByTag("input")[2].attr("value").toString())

                                val add = Jsoup.connect("https://www.racedayquads.com/cart/add.js")
                                    .data(productDataMap)
                                    .cookies(cookies)
                                    .method(Connection.Method.POST)
                                    .followRedirects(true)
                                    .execute()

                                Log.d("out", "added " + add.statusCode().toString())

                                dismiss()
                            }
                        }
                    }
                }
                super.onPageFinished(view, url)
            }
        }

        return root
    }

    fun getCookieMap(siteName: String): Map<String,String> {

        val manager = CookieManager.getInstance()
        val map = mutableMapOf<String,String>()

        manager.getCookie(siteName)?.let {cookies ->
            val typedArray = cookies.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (element in typedArray) {
                val split = element.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                if(split.size >= 2) {
                    map[split[0]] = split[1]
                } else if(split.size == 1) {
                    map[split[0]] = ""
                }
            }
        }

        return map
    }
}
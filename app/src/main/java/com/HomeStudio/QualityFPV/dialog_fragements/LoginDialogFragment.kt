package com.HomeStudio.QualityFPV.dialog_fragements

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.data.SiteSelectorViewModel
import org.jetbrains.anko.doAsync
import org.jsoup.Connection
import org.jsoup.Jsoup

// Dialog fragment that has the user login to the website passed in and adds the product that initially called this dialog to their account's cart
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

        // Go to website depending on which site is selected in the drawer menu and have user login
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

                // Depending on the website add product to cart after logging in
                when(mSiteSelectorViewModel.website.value){
                    "GetFpv" ->
                    {
                        doAsync {

                            // Customer successfully logs in
                            if(url == "https://www.getfpv.com/customer/account/"){

                                // Get cookies from product page
                                val page = Jsoup.connect(prodUrl)
                                    .method(Connection.Method.GET)
                                    .cookies(cookies)
                                    .execute()

                                cookies = page.cookies()

                                val parsed = page.parse()

                                // Form data to be passed for POST request of adding product to cart
                                val productDataMap: HashMap<String, String> = hashMapOf()
                                productDataMap["form_key"] = parsed.getElementsByClass("product-essential")[0].getElementsByTag("input")[0].attr("value").toString()
                                productDataMap["product"] = parsed.getElementsByClass("product-essential")[0].getElementsByTag("input")[1].attr("value").toString()
                                productDataMap["qty"] = "1"

                                // Add product to cart via post method and form data
                                Jsoup.connect(parsed.getElementsByClass("product-essential")[0].getElementsByTag("form").attr("action").toString())
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

                            // Customer successfully logs in
                            if(url == "https://pyrodrone.com/account") {

                                val page = Jsoup.connect(prodUrl)
                                    .method(Connection.Method.GET)
                                    .cookies(cookies)
                                    .execute()

                                cookies = page.cookies()

                                // Form data to be passed for POST request of adding product to cart
                                val productDataMap: HashMap<String, String> = hashMapOf()
                                productDataMap["id"] = page.parse().getElementsByTag("option").attr("value").toString()
                                productDataMap["quantity"] = "1"

                                // Add product to cart via post method and form data
                                val add = Jsoup.connect("https://pyrodrone.com/cart/add.js")
                                    .data(productDataMap)
                                    .cookies(cookies)
                                    .method(Connection.Method.POST)
                                    .followRedirects(true)
                                    .execute()

                                dismiss()
                            }
                        }
                    }

                    "RaceDayQuads" ->
                    {
                        doAsync {

                            // Customer successfully logs in
                            if(url == "https://www.racedayquads.com/account"){

                                val page = Jsoup.connect(prodUrl)
                                    .method(Connection.Method.GET)
                                    .cookies(cookies)
                                    .execute()

                                cookies = page.cookies()

                                val parsed = page.parse()

                                // Form data to be passed for POST request of adding product to cart
                                val productDataMap: HashMap<String, String> = hashMapOf()
                                productDataMap["form_type"] = "product"
                                productDataMap["utf8"] = "e2 9c 93"
                                productDataMap["id"] = parsed.getElementsByClass("product-main")[0].getElementsByTag("input")[2].attr("value").toString()
                                productDataMap["quantity"] = "1"


                                // Add product to cart via post method and form data
                                val add = Jsoup.connect("https://www.racedayquads.com/cart/add.js")
                                    .data(productDataMap)
                                    .cookies(cookies)
                                    .method(Connection.Method.POST)
                                    .followRedirects(true)
                                    .execute()

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

    // Creates a cookie map given a url from the webview
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
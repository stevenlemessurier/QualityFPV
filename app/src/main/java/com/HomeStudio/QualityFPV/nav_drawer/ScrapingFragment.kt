package com.HomeStudio.QualityFPV.nav_drawer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.RecyclerViewAdapter
import com.HomeStudio.QualityFPV.data.Product
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.coroutines.flow.callbackFlow
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.*
import kotlin.concurrent.thread
import kotlin.properties.Delegates


@SuppressLint("SetJavaScriptEnabled")
open class ScrapingFragment: Fragment() {

    private lateinit var mProductViewModel: ProductViewModel
    private var productList: MutableList<Product> = mutableListOf()
    private lateinit var browser: WebView
    private lateinit var prodType: String
    private lateinit var recycView: RecyclerView
    private var pageNumber = 2
    private var prevPage = 1
    private lateinit var website: String
    private var minPrice by Delegates.notNull<Double>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Log.d("out", "Scraping Fragment Opening")
    }


    private inner class JSHtmlInterface(context: Context,
        val productType: String) {

        @JavascriptInterface
        fun showHTML(html: String) {

            mProductViewModel = ViewModelProvider(this@ScrapingFragment).get(ProductViewModel::class.java)

            Log.d("out", "Starting parse")

            if(website == "Pyro Drone") {
                val doc = Jsoup.parse(html)
                val elements: Elements = doc.getElementsByClass("grid-view-item")

                var i = 0
                for (product in elements) {
                    i++
                    Log.d("out", "Item number $i")

                    var highResSrc = product.getElementsByTag("img")[0].attr("src")
                        .replace("200x.jpg", "740x.jpg")
                    highResSrc = highResSrc.replace("200x.png", "740x.png")

                    productList.add(
                        Product(
                            product.getElementsByClass("grid-view-item__title")[0].text(),
                            productType,
                            "https:".plus(highResSrc),
                            product.getElementsByClass("product-price__price")[0].text(),
                            (if (product.getElementsByClass("yotpo-bottomline").text().length > 1)
                                product.getElementsByClass("yotpo-bottomline").text()
                                    .substring(0, 3)
                                    .toDouble() * product.getElementsByClass("yotpo-bottomline")[0].getElementsByClass(
                                    "text-m"
                                )[0].text().dropLast(7).toDouble()
                            else 0.0),
                            "https://pyrodrone.com".plus(product.getElementsByTag("a").attr("href")),
                            website
                        )
                    )
                }

                if (doc.getElementsByClass("next").isNotEmpty()) {
                    Log.d("out", "Found next")
                    Log.d("out", productList.size.toString())
                    pageNumber++
                }
                else {
                    Log.d("out", productList.size.toString())
                    productList.sortByDescending {
                        it.rating
                    }

                    var prodCount = 0
                    var i = 0
                    while(prodCount < 10 ){
                        val product = productList[i].price.substring(1, productList[i].price.lastIndex)
                        if(product.toDouble() > minPrice) {
                            mProductViewModel.addProduct(productList[i])
                            prodCount++
                            i++
                        }
                        else
                            i++
                    }

                    doAsync {
                        uiThread {
                            (activity as MainActivity).toggleProgressBar(false)
                        }
                    }
                }
            }

            else if(website == "GetFpv"){
                val doc = Jsoup.parse(html)
                val allInfo = doc.getElementsByClass("item")

                for (i in allInfo) {

                    val highResSrc = i.getElementsByTag("img").attr("src")
                        .replace("small_image/20x", "image")

                    productList.add(
                        Product(
                            i.getElementsByClass("product-name").text(),
                            productType,
                            highResSrc,
                            (if (i.getElementsByClass("price").size > 1)
                                i.getElementsByClass("price")[1].text()
                            else i.getElementsByClass("price").text()),
                            (if (i.getElementsByClass("yotpo-bottomline").text().length > 1)
                                i.getElementsByClass("yotpo-bottomline").text()
                                    .substring(0, 3)
                                    .toDouble() * i.getElementsByClass("yotpo-bottomline")[0].getElementsByClass(
                                    "text-m"
                                )[0].text().dropLast(7).toDouble()
                            else 0.0),
                            i.getElementsByTag("a").attr("href"),
                            website
                        )
                    )
                }

                if (doc.getElementsByClass("next").isNotEmpty()) {
                    Log.d("out", "Found next")
                    Log.d("out", productList.size.toString())
                    pageNumber++
                }
                else {
                    Log.d("out", productList.size.toString())
                    productList = productList.distinct().toMutableList()
                    productList.sortByDescending {
                        it.rating
                    }

                    var prodCount = 0
                    var i = 0
                    while(prodCount < 10 ){
                        val product = productList[i].price.substring(1, productList[i].price.lastIndex)
                        if(product.toDouble() > minPrice) {
                            mProductViewModel.addProduct(productList[i])
                            prodCount++
                            i++
                        }
                        else
                            i++
                    }

                    doAsync {
                        uiThread {
                            (activity as MainActivity).toggleProgressBar(false)
                        }
                    }
                }
            }

            else if(website == "RaceDayQuads"){

                val doc = Jsoup.parse(html)
                val allInfo = doc.getElementsByClass("productgrid--item")

                Log.d("out", doc.html())

                for (i in allInfo) {
                    Log.d("out", "item ${i.elementSiblingIndex()}")
                    productList.add(
                        Product(
                            i.getElementsByClass("productitem--title").text(),
                            productType,
                            "https:".plus(i.getElementsByTag("img")[0].attr("src")),
                            (if(i.getElementsByClass("money").size > 1)
                                i.getElementsByClass("money")[1].text()
                            else
                                i.getElementsByClass("money").text()) ,
                            (if (i.getElementsByClass("yotpo-bottomline").text().length > 1)
                                i.getElementsByClass("yotpo-bottomline").text()
                                    .substring(0, 3)
                                    .toDouble() * i.getElementsByClass("yotpo-bottomline")[0].getElementsByClass(
                                    "text-m"
                                )[0].text().dropLast(7).toDouble()
                            else 0.0),
                            "https://racedayquads.com".plus(i.getElementsByTag("a").attr("href")),
                            website
                        )
                    )


                }

                if (doc.getElementsByClass("pagination--next").isNotEmpty()) {
                    Log.d("out", "Found next")
                    Log.d("out", productList.size.toString())
                    pageNumber++
                } else {
                    Log.d("out", productList.size.toString())
                    productList = productList.distinct().toMutableList()


                    var prodCount = 0
                    var i = 0
                    while(prodCount < 10 ){
                        val product = productList[i].price.substring(1, productList[i].price.lastIndex)
                        if(product.toDouble() > minPrice) {
                            mProductViewModel.addProduct(productList[i])
                            prodCount++
                            i++
                        }
                        else
                            i++
                    }

                    doAsync {
                        uiThread {
                            (activity as MainActivity).toggleProgressBar(false)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun getProducts(productType: String, recyclerView: RecyclerView, site: String, min: Double){

        when(site){
            "Pyro Drone" -> doAsync {
                uiThread {
                    recyclerView.background = resources.getDrawable(R.drawable.background_pyrodrone)
                }
            }

            "GetFpv" ->
                doAsync {
                    uiThread {
                        recyclerView.background = resources.getDrawable(R.drawable.background_getfpv)
                    }
                }

            "RaceDayQuads" -> doAsync {
                uiThread {
                    recyclerView.background = resources.getDrawable(R.drawable.background_rdq)
                }
            }
        }
        prevPage = 1
        pageNumber = 2
        productList.clear()
        minPrice = min

        website = site
        Log.d("out", website)
        prodType = productType
        recycView = recyclerView
        mProductViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        browser = WebView(requireContext())
        browser.apply {
            visibility = View.INVISIBLE
            setLayerType(View.LAYER_TYPE_NONE, null)
            settings.javaScriptEnabled = true
            settings.blockNetworkImage = true
            settings.domStorageEnabled = false
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.loadsImagesAutomatically = false
            settings.setGeolocationEnabled(false)
            settings.setSupportZoom(false)
        }

        val jInterface = JSHtmlInterface(requireContext(), productType)
        browser.addJavascriptInterface(jInterface, "HtmlViewer")

        browser.webViewClient = object: WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.d("out", "Starting page")
                (activity as MainActivity).setProgressText("Loading page $prevPage...")
                super.onPageStarted(view, url, favicon)
            }

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView?, url: String?) {
//                browser.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
//                Log.d("out", "page loaded")

                browser.evaluateJavascript("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');",null)
                Log.d("out", "page loaded")



                if(pageNumber > prevPage) {
                    prevPage++
                    Log.d("out", "Loading page $pageNumber")
                    when(site) {
                        "Pyro Drone" -> browser.loadUrl ("https://pyrodrone.com/collections/$productType?page=$pageNumber")
                        "GetFpv" -> browser.loadUrl ("https://www.getfpv.com/$productType.html?limit=100&p=$pageNumber")
                        "RaceDayQuads" -> browser.loadUrl("https://www.racedayquads.com/collections/$productType?page=$pageNumber&sort_by=best-selling")
                    }
                    (activity as MainActivity).toggleProgressBar(true)
                }
            }
        }

        mProductViewModel.getProduct(productType, website).observe(viewLifecycleOwner, {
            if (it == null) {
                doAsync {
                    uiThread {
                        (activity as MainActivity).toggleProgressBar(true)
                    }
                }
                when(site) {
                    "Pyro Drone" -> browser.loadUrl ("https://pyrodrone.com/collections/$productType?page=1")
                    "GetFpv" -> browser.loadUrl ("https://www.getfpv.com/$productType.html?limit=100&p=1")
                    "RaceDayQuads" -> browser.loadUrl("https://www.racedayquads.com/collections/$productType?page=1&sort_by=best-selling")
                }
                Log.d("out", "Loading Url")
                (activity as MainActivity).setProgressText("Loading page $prevPage")
            }
            else
                (activity as MainActivity).toggleProgressBar(false)
        })



        doAsync {
            if (activity != null) {
                uiThread {
                    val productRecyclerViewAdapter = RecyclerViewAdapter(mProductViewModel)
                    val productLinearLayoutManager = LinearLayoutManager(
                        activity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )

                    mProductViewModel.readAllProductType(productType, website).observe(
                        viewLifecycleOwner,
                        { product ->
                            productRecyclerViewAdapter.setData(product)
                        })

                    recyclerView.layoutManager = productLinearLayoutManager
                    recyclerView.adapter = productRecyclerViewAdapter
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refresh).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.refresh -> {
                prevPage = 1
                pageNumber = 2
                productList = mutableListOf()
                mProductViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

                doAsync {
                    uiThread {
                        mProductViewModel.deleteProducts(prodType)
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
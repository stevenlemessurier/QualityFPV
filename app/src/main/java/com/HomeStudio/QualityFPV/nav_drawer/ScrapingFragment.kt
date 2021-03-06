package com.HomeStudio.QualityFPV.nav_drawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.data.FilterViewModel
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.RecyclerViewAdapter
import com.HomeStudio.QualityFPV.data.product.Product
import com.HomeStudio.QualityFPV.data.product.ProductViewModel
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.content_main.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.*
import kotlin.properties.Delegates

// Parent fragment that holds logic for scraping webpages for product data
@SuppressLint("SetJavaScriptEnabled")
open class ScrapingFragment: Fragment() {

    private lateinit var mProductViewModel: ProductViewModel
    private lateinit var mFilterViewModel: FilterViewModel
    private var productList: MutableList<Product> = mutableListOf()
    private lateinit var browser: WebView
    lateinit var prodType: String
    private lateinit var recycView: RecyclerView
    private var pageNumber = 2
    private var prevPage = 1
    private lateinit var website: String
    private var minPrice by Delegates.notNull<Double>()
    private var maxPrice by Delegates.notNull<Double>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // Used to get Javascript data from websites such as product ratings
    private inner class JSHtmlInterface(context: Context,
        val productType: String) {

        // Pulls and scrapes the html from each website for top 10 rated products in a category and pushes them into the Room Database
        @JavascriptInterface
        fun showHTML(html: String) {

            // Initialize instances of product view model and filter view model to manage products and the filter prices
            mProductViewModel = ViewModelProvider(this@ScrapingFragment).get(ProductViewModel::class.java)
            mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

            minPrice = mFilterViewModel.min.value?.toDouble() ?: 0.0
            maxPrice = mFilterViewModel.max.value?.toDouble() ?: 10000.0

            Log.d("out", "Starting parse")

            if(website == "Pyro Drone") {
                val doc = Jsoup.parse(html)
                val elements: Elements = doc.getElementsByClass("grid-view-item")

                var i = 0
                for (product in elements) {
                    i++
                    Log.d("out", "Item number $i")

                    // Get correct sized image to display
                    var highResSrc = product.getElementsByTag("img")[0].attr("src")
                        .replace("200x.jpg", "740x.jpg")
                    highResSrc = highResSrc.replace("200x.png", "740x.png")

                    // Parse page for product data and add product object to list to pass to recycler view
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

                // If there are more pages to parse, continue loading/scraping pages otherwise sort products collected by rating and send to recycler view
                if (doc.getElementsByClass("next").isNotEmpty()) {
                    pageNumber++
                }
                else {
                    productList.sortByDescending {
                        it.rating
                    }

                    var prodCount = 0
                    var i = 0

                    // Add product to database if it meets price range
                    while(prodCount < 10 ){
                        val product = productList[i].price.substring(1, productList[i].price.lastIndex)
                        if(product.toDouble() > minPrice && product.toDouble() < maxPrice) {
                            mProductViewModel.addProduct(productList[i])
                            prodCount++
                            i++
                        }
                        else
                            i++
                    }

                    // Hide progress bar
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
                    // Get correct size image to display
                    val highResSrc = i.getElementsByTag("img").attr("src")
                        .replace("small_image/20x", "image")

                    // Parse page for product data and add product object to list to pass to recycler view
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

                // If there are more pages to parse, continue loading/scraping pages otherwise sort products collected by rating and send to recycler view
                if (doc.getElementsByClass("next").isNotEmpty()) {
                    pageNumber++
                }
                else {
                    productList = productList.distinct().toMutableList()
                    productList.sortByDescending {
                        it.rating
                    }

                    var prodCount = 0
                    var i = 0
                    // Add product to database if it meets price range
                    while(prodCount < 10 ){
                        val product = productList[i].price.substring(1, productList[i].price.lastIndex)
                        if(product.toDouble() > minPrice && product.toDouble() < maxPrice) {
                            mProductViewModel.addProduct(productList[i])
                            prodCount++
                            i++
                        }
                        else
                            i++
                    }

                    // Hide progress bar
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

                for (i in allInfo) {
                    Log.d("out", "item ${i.elementSiblingIndex()}")
                    // Parse page for product data and add product object to list to pass to recycler view
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

                // If there are more pages to parse, continue loading/scraping pages otherwise sort products collected by rating and send to recycler view
                if (doc.getElementsByClass("pagination--next").isNotEmpty()) {
                    pageNumber++
                } else {
                    Log.d("out", productList.size.toString())
                    productList = productList.distinct().toMutableList()


                    var prodCount = 0
                    var i = 0
                    // Add product to database if it meets price range
                    while(prodCount < 10 ){
                        val product = productList[i].price.substring(1, productList[i].price.lastIndex)
                        if(product.toDouble() > minPrice && product.toDouble() < maxPrice) {
                            mProductViewModel.addProduct(productList[i])
                            prodCount++
                            i++
                        }
                        else
                            i++
                    }

                    // Hide progress bar
                    doAsync {
                        uiThread {
                            (activity as MainActivity).toggleProgressBar(false)
                        }
                    }
                }
            }
        }
    }


    // Manages scraping and recycler view loading of product type passed in
    @SuppressLint("SetJavaScriptEnabled")
    fun getProducts(productType: String, recyclerView: RecyclerView, site: String){

        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        // Set background based on site selected
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

        website = site
        prodType = productType
        recycView = recyclerView
        mProductViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        // Setup webview for scraping javascript data
        browser = WebView(requireContext())
        browser.apply {
            visibility = View.INVISIBLE
            setLayerType(View.LAYER_TYPE_NONE, null)
            settings.javaScriptEnabled = true
            settings.blockNetworkImage = true
            settings.domStorageEnabled = true
            settings.allowUniversalAccessFromFileURLs
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.loadsImagesAutomatically = false
            settings.setGeolocationEnabled(false)
            settings.setSupportZoom(false)
        }

        val jInterface = JSHtmlInterface(requireContext(), productType)
        browser.addJavascriptInterface(jInterface, "HtmlViewer")

        browser.webViewClient = object: WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                (activity as MainActivity).setProgressText("Loading page $prevPage...")
                super.onPageStarted(view, url, favicon)
            }


            // Scrape all pages of product type pulling top 10 rated items
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView?, url: String?) {

                browser.evaluateJavascript("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');",null)

                if(pageNumber > prevPage) {
                    prevPage++
                    when(site) {
                        "Pyro Drone" -> browser.loadUrl ("https://pyrodrone.com/collections/$productType?page=$pageNumber")
                        "GetFpv" -> browser.loadUrl ("https://www.getfpv.com/$productType.html?limit=100&p=$pageNumber")
                        "RaceDayQuads" -> browser.loadUrl("https://www.racedayquads.com/collections/$productType?page=$pageNumber&sort_by=best-selling")
                    }
                    (activity as MainActivity).toggleProgressBar(true)
                }
            }
        }

        // Rescrape pages if user selects new range of price
        mFilterViewModel.change.observe(viewLifecycleOwner, {
            if(it) {
                productList = mutableListOf()
                mFilterViewModel.setChange(false)
                doAsync {
                    uiThread {
                        mProductViewModel.deleteProducts(prodType)
                    }
                }
            }
        })

        mProductViewModel.getProduct(productType, website).observe(viewLifecycleOwner, {
            if (it == null) {
                doAsync {
                    uiThread {
                        (activity as MainActivity).toggleProgressBar(true)
                    }
                }
                prevPage = 1
                pageNumber = 2
                when(site) {
                    "Pyro Drone" -> browser.loadUrl ("https://pyrodrone.com/collections/$productType?page=1")
                    "GetFpv" -> browser.loadUrl ("https://www.getfpv.com/$productType.html?limit=100&p=1")
                    "RaceDayQuads" -> browser.loadUrl("https://www.racedayquads.com/collections/$productType?page=1&sort_by=best-selling")
                }
                (activity as MainActivity).setProgressText("Loading page $prevPage")
            }
            else
                (activity as MainActivity).toggleProgressBar(false)
        })



        doAsync {
            if (activity != null) {
                uiThread {
                    lateinit var  productRecyclerViewAdapter: RecyclerViewAdapter
                    lateinit var productLinearLayoutManager: LinearLayoutManager

                    // Setup recycler view based on orientation of device
                    when(resources.configuration.orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> {
                            productRecyclerViewAdapter = RecyclerViewAdapter(false)
                            productLinearLayoutManager = LinearLayoutManager(
                                activity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                        Configuration.ORIENTATION_PORTRAIT -> {
                            productRecyclerViewAdapter = RecyclerViewAdapter(false)
                            productLinearLayoutManager = LinearLayoutManager(
                                activity,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }
                    }

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

    // Show refresh button in app bar
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refresh).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }


    // Rescrape for data if refresh button is pressed
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
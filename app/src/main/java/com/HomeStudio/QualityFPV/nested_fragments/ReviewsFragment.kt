package com.HomeStudio.QualityFPV.nested_fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.ReviewAdapter
import com.HomeStudio.QualityFPV.data.product.Product
import com.HomeStudio.QualityFPV.data.Review
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.select.Elements

// This fragment displays reviews of a product passed in by scraping the product page
open class ReviewsFragment: Fragment() {

    private lateinit var product: Product
    private lateinit var browser: WebView


    // Scrape javascript html of product page since reviews are loaded onto page through javascript
    private inner class JSHtmlInterface(context: Context, val recyclerView: RecyclerView, val progressCard: CardView) {
        @JavascriptInterface
        fun showHTML(html: String) {

            val doc = Jsoup.parse(html)
            val elements: Elements = doc.getElementsByClass("yotpo-review")
            val reviewList = mutableListOf<Review>()

            var i = 0
            for (review in elements) {

                if(i == 0){
                    i++
                    continue
                }

                i++

                val reviewTitle = review.getElementsByClass("content-title")[0].text()
                val reviewDetails = review.getElementsByClass("content-review")[0].text()
                val reviewRating = review.getElementsByClass("sr-only").text().substring(0,3).toDouble()
                val reviewDate = review.getElementsByClass("y-label")[2].text()

                val newReview = Review(
                    reviewTitle,
                    reviewRating,
                    reviewDetails,
                    reviewDate)

                reviewList.add(newReview)
            }

            // Load and display recycler view items with list of reviews scraped
            doAsync {
                if (activity != null) {
                    uiThread {
                        val reviewRecyclerViewAdapter = ReviewAdapter(reviewList)
                        val reviewLinearLayoutManager = LinearLayoutManager(
                            activity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )

                        recyclerView.layoutManager = reviewLinearLayoutManager
                        recyclerView.adapter = reviewRecyclerViewAdapter
                        progressCard.visibility = View.GONE
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // Get product data passed in or saved on orientation change
        if (savedInstanceState != null)
            product = savedInstanceState.getParcelable<Product>("productData") as Product
        else {
            val bundle = this.arguments
            if (bundle != null)
                product = bundle.getParcelable<Product>("productData") as Product
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_reviews, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.reviews_recyclerview)
        val progress = root.findViewById<CardView>(R.id.revie_progress_card)

        // Setup webview to load javascript and scrape from
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

        val jInterface = JSHtmlInterface(requireContext(), recyclerView, progress)
        browser.addJavascriptInterface(jInterface, "HtmlViewer")

        browser.webViewClient = object: WebViewClient() {

            // Show progress wheel while loading page
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progress.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }


            // Scrape html page for reviews after javascript data has been loaded
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView?, url: String?) {
                browser.evaluateJavascript("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');",null)
            }
        }

        browser.loadUrl(product.url)

        return root
    }


    // Hides refresh button in app bar
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refresh).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }
}
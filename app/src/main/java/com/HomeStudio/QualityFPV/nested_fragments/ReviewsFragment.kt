package com.HomeStudio.QualityFPV.nested_fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Html
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.RecyclerViewAdapter
import com.HomeStudio.QualityFPV.adapters.ReviewAdapter
import com.HomeStudio.QualityFPV.data.Product
import com.HomeStudio.QualityFPV.data.ProductViewModel
import com.HomeStudio.QualityFPV.data.Review
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.select.Elements

open class ReviewsFragment: Fragment() {

    private lateinit var product: Product
    private lateinit var browser: WebView


    private inner class JSHtmlInterface(context: Context, val recyclerView: RecyclerView, val progressCard: CardView) {
        @JavascriptInterface
        fun showHTML(html: String) {

            //Log.d("out", "Starting parse")
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
                //Log.d("out", "Item number $i")

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

                //Log.d("out", newReview.toString())
            }

            //Log.d("out", "${reviewList.size} reviews found")


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

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.d("out", "Starting page")
                progress.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onPageFinished(view: WebView?, url: String?) {
                browser.evaluateJavascript("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');",null)
                Log.d("out", "page loaded")
            }
        }

        browser.loadUrl(product.url)

        return root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refresh).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }
}
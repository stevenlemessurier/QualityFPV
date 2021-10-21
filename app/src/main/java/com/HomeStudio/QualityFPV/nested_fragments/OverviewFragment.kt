package com.HomeStudio.QualityFPV.nested_fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.DetailImageAdapter
import com.HomeStudio.QualityFPV.data.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_product.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class OverviewFragment: Fragment() {

    private lateinit var product: Product
    var isIntent = false

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_overview, container, false)

        val productName = root.findViewById<TextView>(R.id.detail_product_name)
        val productDetails = root.findViewById<TextView>(R.id.detail_product_details)
        val progressBar = root.findViewById<ProgressBar>(R.id.product_progress_bar)
        val images = root.findViewById<RecyclerView>(R.id.detail_product_images)
        val prevButton = root.findViewById<ImageButton>(R.id.prev_button)
        val nextButton = root.findViewById<ImageButton>(R.id.next_button)
        val fab = root.findViewById<FloatingActionButton>(R.id.product_fab)

        fab.setOnClickListener {
            goToUrl(product.url)
        }

        doAsync {
            val doc = Jsoup.connect(product.url).get()

            val all = doc.getElementsByClass("product-description")[0].select("p, li, h2, h3")
            var htmlDetails = parseDetails(all)

            lateinit var detailImages: Elements
            val imgList = mutableListOf<String>()

            if (all.isEmpty()) {
                val newAll = doc.getElementsByClass("product-description")[0].html()

                htmlDetails = Html.fromHtml(newAll).toString()
            }
//
//            val descripImages = doc.getElementById("tab1").getElementsByTag("img")
//            for (image in descripImages)
//                descriptionImages.add(image.attr("src").toString())
//
//            val descripVideos = doc.getElementById("tab1").getElementsByTag("iframe")
//            for (video in descripVideos) {
//                var videoUri = video.attr("src").toString()
//
//                if (videoUri.length > 11) {
//                    videoUri = videoUri.substring(videoUri.length - 11, videoUri.length)
//                    descriptionVideos.add(videoUri)
//                }
//            }
//
            detailImages = doc.getElementsByClass("text-link")
            for (img in detailImages)
                imgList.add("https:${img.attr("href")}")

            if (imgList.isEmpty()) {
                detailImages = doc.getElementsByClass("prod-large-img")
                for (img in detailImages)
                    imgList.add("https:${img.getElementsByTag("img").attr("src")}")
            }

//            val cost = "$" + doc.getElementsByAttributeValue(
//                "property",
//                "og:price:amount"
//            )[0].attr("content").toString()

            uiThread {
                productName.text = product.name
                if (htmlDetails.isEmpty())
                    productDetails.text = "No Description"
                else
                    productDetails.text = htmlDetails

                //Setup RecyclerView
                val detailRecyclerViewAdapter = DetailImageAdapter(imgList)
                val detailLinearLayoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                images.layoutManager = detailLinearLayoutManager
                images.adapter = detailRecyclerViewAdapter
                LinearSnapHelper().attachToRecyclerView(detail_product_images)

                // Logic to show and hide side arrows of images from recycler view of item
                images.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                    override fun onScrollStateChanged(
                        recyclerView: RecyclerView,
                        newState: Int
                    ) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && detailLinearLayoutManager.findFirstVisibleItemPosition() == detailRecyclerViewAdapter.itemCount - 1) {
                            nextButton.visibility = View.GONE
                        } else if (newState == RecyclerView.SCROLL_STATE_IDLE && detailLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                            prevButton.visibility = View.GONE

                        } else if (newState == RecyclerView.SCROLL_STATE_SETTLING && detailLinearLayoutManager.itemCount != 1) {
                            prevButton.visibility = View.VISIBLE
                            nextButton.visibility = View.VISIBLE
                        }
                    }
                })

                // Previous button for images of item recycler view logic
                prevButton.setOnClickListener {
                    if (detailLinearLayoutManager.findFirstVisibleItemPosition() != 1) {
                        images.scrollToPosition(detailLinearLayoutManager.findFirstVisibleItemPosition() - 1)
                        prevButton.visibility = View.VISIBLE
                        nextButton.visibility = View.VISIBLE
                    } else {
                        prevButton.visibility = View.GONE
                        nextButton.visibility = View.VISIBLE
                        images.scrollToPosition(detailLinearLayoutManager.findFirstVisibleItemPosition() - 1)
                    }
                }

                // Next button for images of item recycler view logic
                nextButton.setOnClickListener {
                    if (detailLinearLayoutManager.findFirstVisibleItemPosition() != detailRecyclerViewAdapter.itemCount - 2) {
                        images.scrollToPosition(detailLinearLayoutManager.findFirstVisibleItemPosition() + 1)
                        nextButton.visibility = View.VISIBLE
                        prevButton.visibility = View.VISIBLE
                    } else {
                        nextButton.visibility = View.GONE
                        prevButton.visibility = View.VISIBLE
                        images.scrollToPosition(detailLinearLayoutManager.findFirstVisibleItemPosition() + 1)
                    }
                }

                if (detailRecyclerViewAdapter.itemCount != 1) {
                    nextButton.visibility = View.VISIBLE
                }

                productName.visibility = View.VISIBLE
                productDetails.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                fab.visibility = View.VISIBLE
            }
        }

        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("productData", product)
        super.onSaveInstanceState(outState)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refresh).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    private fun goToUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        isIntent = true
        startActivity(i)
        isIntent = false
    }

    // Takes in elements to parse and returns a formatted description of the item
    private fun parseDetails(elements: Elements): String {

        var htmlDetails = ""
        var num = 0
        var parsedList = mutableListOf<String>()
        for (i in elements) {
            if (i.tagName() == "h2" || i.tagName() == "h3")
                parsedList.add("$num")
            parsedList.add(i.text())
            num++
        }

        var prev = ""
        if (parsedList.isNotEmpty()) {
            parsedList = parsedList.distinct() as MutableList<String>

            for (i in parsedList) {
                if (prev != "" && (prev.contains(i) || i.contains(prev)))
                    htmlDetails = htmlDetails
                else {
                    htmlDetails = if (i.toIntOrNull() != null)
                        htmlDetails + "\n"
                    else
                        htmlDetails + "\n" + i

                    prev = i
                }
            }
        }

        return htmlDetails
    }
}
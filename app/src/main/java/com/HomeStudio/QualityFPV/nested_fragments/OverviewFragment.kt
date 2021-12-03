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
import com.HomeStudio.QualityFPV.adapters.DescriptionImageAdapter
import com.HomeStudio.QualityFPV.adapters.DetailImageAdapter
import com.HomeStudio.QualityFPV.data.product.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_overview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.select.Elements

// Fragment to display details of product once selected from a main product type fragment
class OverviewFragment: Fragment() {

    private lateinit var product: Product
    var isIntent = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // Retrieve product data passed in or saved on orientation change
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
        val prodDescImages = root.findViewById<RecyclerView>(R.id.product_details_images)
        val prevButton = root.findViewById<ImageButton>(R.id.prev_button)
        val nextButton = root.findViewById<ImageButton>(R.id.next_button)
        val fab = root.findViewById<FloatingActionButton>(R.id.product_fab)

        // Open product page in device browser
        fab.setOnClickListener {
            goToUrl(product.url)
        }

        doAsync {

            lateinit var detailImages: Elements
            val imgList = mutableListOf<String>()
            val descriptionImages = mutableListOf<String>()
            lateinit var htmlDetails: String

            val doc = Jsoup.connect(product.url).get()

            // Scrape product page by website
            when(product.website) {

                "Pyro Drone" -> {

                    val all = doc.getElementsByClass("product-description")[0].select("p, li, h2, h3")

                    // Get description of product
                    htmlDetails = parseDetails(all)
                    if (all.isEmpty()) {
                        val newAll = doc.getElementsByClass("product-description")[0].html()

                        htmlDetails = Html.fromHtml(newAll).toString()
                    }

                    // Get images in the description of the product
                    val descripImages = doc.getElementById("tab1").getElementsByTag("img")
                    for (image in descripImages)
                        descriptionImages.add(image.attr("src").toString())

                    // Get product images
                    detailImages = doc.getElementsByClass("text-link")
                    for (img in detailImages)
                        imgList.add("https:${img.attr("href")}")

                    if (imgList.isEmpty()) {
                        detailImages = doc.getElementsByClass("prod-large-img")
                        for (img in detailImages)
                            imgList.add("https:${img.getElementsByTag("img").attr("src")}")
                    }
                }

                "GetFpv" -> {

                    // Get description of product
                    val all = doc.getElementsByClass("std")[1].select("p, li, h2, h3")
                    htmlDetails = parseDetails(all)

                    // Get images in the description of the product
                    val descripImages = doc.getElementById("collateral-tabs").getElementsByTag("img")
                    for (image in descripImages) {
                        descriptionImages.add(image.attr("src").toString())
                    }

                    // Get images of product
                    detailImages = doc.getElementsByClass("thumb-link")
                    for (img in detailImages)
                        imgList.add(img.attr("href"))
                }

                "RaceDayQuads" -> {

                    // Get description of product
                    val all = doc.getElementById("tabs-2").select("p, span, li, h3, h2")
                    htmlDetails = parseDetails(all)

                    // Get images in the description of the product
                    val descripImages = doc.getElementById("tabs-2").getElementsByTag("img")
                    for (image in descripImages) {
                        descriptionImages.add(image.attr("src").toString())
                    }

                    // Get images of product
                    val detailIm = doc.getElementsByClass("product-gallery--thumbnail-trigger")
                    for (img in detailIm) {
                        imgList.add(
                            "https:${img.getElementsByTag("img").attr("src")}".replace(
                                "47x47",
                                "992x992"
                            )
                        )
                    }
                }
            }


            uiThread {

                // Set description, description images, and product images to recycler views
                productName.text = product.name
                if (htmlDetails.isEmpty())
                    productDetails.text = "No Description"
                else
                    productDetails.text = htmlDetails

                val detailImageRecyclerViewAdapter = DescriptionImageAdapter(descriptionImages)
                val detailImageLinearLayoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                prodDescImages.adapter = detailImageRecyclerViewAdapter
                prodDescImages.layoutManager = detailImageLinearLayoutManager

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


    // Save state data on orientation change
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("productData", product)
        super.onSaveInstanceState(outState)
    }


    // Hides the refresh button in the app bar
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refresh).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }


    // Sends user to product page in device browser
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
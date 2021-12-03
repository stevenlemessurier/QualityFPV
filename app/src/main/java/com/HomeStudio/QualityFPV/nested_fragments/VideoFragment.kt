package com.HomeStudio.QualityFPV.nested_fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.VideoAdapter
import com.HomeStudio.QualityFPV.data.product.Product
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import org.jsoup.Jsoup

// Displays youtube videos based on the name of the product passed in
class VideoFragment: Fragment() {

    private lateinit var product: Product


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


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_video, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.video_recycler_view)
        val progress = root.findViewById<CardView>(R.id.video_progress_card)

        // Get api key for youtube stored in local properties file
        val apiKey =  (activity as MainActivity).appInfo.metaData["keyValue"].toString()

        // Create url with api key and product name to search for top 5 results on youtube
        val urlName = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=5&q="+product.name
            .replace(
            " ",
            "+"
        )+"&type=video&key="+apiKey
        val videoList = mutableListOf<String>()

        // Parse response from api to get video IDs
        doAsync {
            val doc = Jsoup.connect(urlName).ignoreContentType(true).get()

            val jsonOuter = JSONObject(doc.toString().replace("<html>", "")
                .replace("<head>", "")
                .replace("</head>", "")
                .replace("<body>", "")
                .replace("</body>", "")
                .replace("</html", "")
                .replace(">", ""))

            val itemsArray = jsonOuter.getJSONArray("items")

            for (jsonIndex in 0 until itemsArray.length()){
                val idObject = itemsArray.getJSONObject(jsonIndex).getJSONObject("id")
                val videoId = idObject.getString("videoId")
                videoList.add(videoId)
            }

            // Pass video IDs to create youtube video objects and display in recycler view
            uiThread {
                val videoRecyclerViewAdapter = VideoAdapter(videoList)
                val videoLinearLayoutManager = LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerView.layoutManager = videoLinearLayoutManager
                recyclerView.adapter = videoRecyclerViewAdapter

                progress.visibility = View.GONE
            }
        }

        return root
    }


    // Hides the refresh button in the app bar
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refresh).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }
}
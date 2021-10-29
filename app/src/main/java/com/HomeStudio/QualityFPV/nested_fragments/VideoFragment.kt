package com.HomeStudio.QualityFPV.nested_fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.adapters.VideoAdapter
import com.HomeStudio.QualityFPV.data.Product
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.net.URL

class VideoFragment: Fragment() {

    private lateinit var product: Product
    private lateinit var browser: WebView

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

        val apiKey =  (activity as MainActivity).appInfo.metaData["keyValue"].toString()
        val urlName = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=5&q="+product.name.replace(
            " ",
            "+"
        )+"&type=video&key="+apiKey
        val videoList = mutableListOf<String>()

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

            Log.d("out", videoList.toString())
        }

        return root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.refresh).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }
}
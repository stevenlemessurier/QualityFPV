package com.HomeStudio.QualityFPV.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.HomeStudio.QualityFPV.R
import com.HomeStudio.QualityFPV.data.Review
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.adapter_product_reviews.view.*
import kotlinx.android.synthetic.main.adapter_product_videos.view.*

class VideoAdapter(private val videoList: MutableList<String>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        this.parent = parent
        return ViewHolder(
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.adapter_product_videos, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = videoList[position]
        val yTPlayer = holder.itemView.product_video

        yTPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(currentItem, 0f)
            }
        })
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
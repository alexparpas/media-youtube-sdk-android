package com.alexparpas.media.youtube.ui.media.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexparpas.media.youtube.R
import com.alexparpas.media.youtube.core.model.CategoryItem
import com.alexparpas.media.youtube.core.model.VideoItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.myt_layout_video_rv_item.view.*

class YouTubeMediaVideosAdapter(private val callback: Callback) : RecyclerView.Adapter<YoutubeMediaVideosViewHolder>() {
    var videos: List<VideoItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeMediaVideosViewHolder {
        return YoutubeMediaVideosViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.myt_layout_video_rv_item, parent, false))
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: YoutubeMediaVideosViewHolder, position: Int) {
        holder.bind(videos[position], callback)
    }

    interface Callback {
        fun onVideoClicked(videoId: String)

        fun onCategoryClicked(category: CategoryItem)
    }
}

class YoutubeMediaVideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: VideoItem, callback: YouTubeMediaVideosAdapter.Callback) {
        if (item.thumbnailUrl.isNotBlank()) {
            Picasso.with(itemView.context).load(item.thumbnailUrl).into(itemView.thumbnail_iv)
        }
        itemView.video_title_tv.text = item.title
        itemView.channel_name_tv.text = item.channelTitle
        itemView.views_tv.text = item.viewCount
        itemView.duration_tv.text = item.duration
        itemView.setOnClickListener { callback.onVideoClicked(item.id) }
    }
}
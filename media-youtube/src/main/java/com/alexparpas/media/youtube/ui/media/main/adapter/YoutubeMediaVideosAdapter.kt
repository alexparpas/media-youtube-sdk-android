package com.alexparpas.media.youtube.ui.media.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexparpas.media.youtube.R
import com.alexparpas.media.youtube.data.CategoryItem
import com.alexparpas.media.youtube.data.VideoMinimal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_video_rv_item.view.*

class YoutubeMediaVideosAdapter(private val callback: Callback) : RecyclerView.Adapter<YoutubeMediaVideosViewHolder>() {
    var videos: List<VideoMinimal> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeMediaVideosViewHolder {
        return YoutubeMediaVideosViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_video_rv_item, parent, false))
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
    fun bind(item: VideoMinimal, callback: YoutubeMediaVideosAdapter.Callback) {
        itemView.setOnClickListener { callback.onVideoClicked(item.id) }

        itemView.channel_name_tv.text = item.channelTitle
        itemView.video_title_tv.text = item.title
        Picasso.get().load(item.thumbnailUrl).into(itemView.thumbnail_iv)
    }
}
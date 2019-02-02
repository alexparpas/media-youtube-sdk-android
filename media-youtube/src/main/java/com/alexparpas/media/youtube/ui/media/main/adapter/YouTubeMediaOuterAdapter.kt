package com.alexparpas.media.youtube.ui.media.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexparpas.media.youtube.R
import com.alexparpas.media.youtube.data.CategoryItem
import com.alexparpas.media.youtube.data.MediaItem
import com.alexparpas.media.youtube.data.VideoMinimal
import com.alexparpas.media.youtube.data.VideosItem
import kotlinx.android.synthetic.main.layout_category_rv_item.view.*
import kotlinx.android.synthetic.main.layout_videos_rv_item.view.*

class YouTubeMediaAdapter(private val callback: YoutubeMediaVideosAdapter.Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var videos: List<MediaItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_CATEGORY) {
            CategoryViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_category_rv_item, parent, false))
        } else {
            VideosViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_videos_rv_item, parent, false))
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (videos[position] is CategoryItem) {
            TYPE_CATEGORY
        } else {
            TYPE_VIDEOS
        }
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_CATEGORY) {
            (holder as CategoryViewHolder).bind((videos[position] as CategoryItem), callback)
        } else {
            (holder as VideosViewHolder).bind((videos[position] as VideosItem).videos, callback)
        }
    }

    companion object {
        const val TYPE_CATEGORY = 0
        const val TYPE_VIDEOS = 1
    }
}

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(categoryItem: CategoryItem, callback: YoutubeMediaVideosAdapter.Callback) {
        itemView.category_tv.apply {
            text = categoryItem.categoryName
            setOnClickListener { callback.onCategoryClicked(categoryItem) }
        }
    }
}

class VideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(videos: List<VideoMinimal>, callback: YoutubeMediaVideosAdapter.Callback) {
        itemView.recycler_view?.apply {
            adapter = YoutubeMediaVideosAdapter(callback).apply { this.videos = videos }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }
}
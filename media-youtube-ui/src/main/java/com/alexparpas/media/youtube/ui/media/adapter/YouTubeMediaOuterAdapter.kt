package com.alexparpas.media.youtube.ui.media.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexparpas.media.youtube.R
import com.alexparpas.media.youtube.core.model.CategoryItem
import com.alexparpas.media.youtube.core.model.MediaItem
import com.alexparpas.media.youtube.core.model.VideoItem
import com.alexparpas.media.youtube.core.model.VideosItem
import kotlinx.android.synthetic.main.myt_layout_category_rv_item.view.*
import kotlinx.android.synthetic.main.myt_layout_videos_rv_item.view.*

internal class YouTubeMediaOuterAdapter(
        private val callback: YouTubeMediaVideosAdapter.Callback,
        private val viewPool: RecyclerView.RecycledViewPool
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var videos: List<MediaItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_CATEGORY) {
            CategoryViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.myt_layout_category_rv_item, parent, false))
        } else {
            VideosViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.myt_layout_videos_rv_item, parent, false))
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
            (holder as VideosViewHolder).bind((videos[position] as VideosItem).videos, callback, viewPool)
        }
    }

    companion object {
        const val TYPE_CATEGORY = 0
        const val TYPE_VIDEOS = 1
    }
}

internal class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(categoryItem: CategoryItem, callback: YouTubeMediaVideosAdapter.Callback) {
        itemView.setOnClickListener { callback.onCategoryClicked(categoryItem) }
        itemView.category_tv.text = categoryItem.title
        itemView.new_tag_iv.visibility = if (categoryItem.isNew) View.VISIBLE else View.GONE
        itemView.description_tv.apply {
            visibility = if (categoryItem.shouldShowDescription) View.VISIBLE else View.GONE
            text = categoryItem.description
        }
    }
}

internal class VideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(videos: List<VideoItem>, callback: YouTubeMediaVideosAdapter.Callback, viewPool: RecyclerView.RecycledViewPool) {
        itemView.recycler_view?.apply {
            setRecycledViewPool(viewPool)
            adapter = YouTubeMediaVideosAdapter(callback).apply { this.videos = videos }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
        }
    }
}
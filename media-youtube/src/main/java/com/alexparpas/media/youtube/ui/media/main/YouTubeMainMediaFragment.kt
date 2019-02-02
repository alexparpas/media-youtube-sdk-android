package com.alexparpas.media.youtube.ui.media.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexparpas.media.youtube.MediaYouTube
import com.alexparpas.media.youtube.R
import com.alexparpas.media.youtube.data.CategoryItem
import com.alexparpas.media.youtube.data.VideoSection
import com.alexparpas.media.youtube.ui.media.main.adapter.YouTubeMediaAdapter
import com.alexparpas.media.youtube.ui.media.main.adapter.YoutubeMediaVideosAdapter
import com.alexparpas.media.youtube.ui.media.more.YouTubeMoreMediaFragment
import kotlinx.android.synthetic.main.fragment_youtube_media.*

class YouTubeMainMediaFragment : Fragment(), YoutubeMediaVideosAdapter.Callback {

    private lateinit var viewModel: YouTubeMediaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_youtube_media, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = YouTubeMediaAdapter(this)
        initRecyclerView(adapter)

        observeVideos(adapter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(
                this,
                MediaYouTube.Injection.provideMediaViewModelFactory(
                        sections = requireNotNull(arguments?.getParcelableArrayList(MediaYouTube.ARG_SECTIONS))
                )
        ).get(YouTubeMediaViewModel::class.java)
    }

    private fun initRecyclerView(adapter: YouTubeMediaAdapter) {
        recycler_view.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeVideos(adapter: YouTubeMediaAdapter) {
        viewModel.mediaLiveData.observe(viewLifecycleOwner, Observer { videos ->
            if (videos != null) {
                adapter.videos = videos
            }
        })
    }

    override fun onVideoClicked(videoId: String) {
        MediaYouTube.playVideo(requireContext(), videoId)
    }

    override fun onCategoryClicked(category: CategoryItem) {
        YouTubeMoreMediaFragment.newInstance(
                category.categoryName,
                ArrayList(category.videoIds)
        ).show(childFragmentManager, "frag")
    }

    companion object {
        fun newInstance(sections: ArrayList<VideoSection>): YouTubeMainMediaFragment {
            return YouTubeMainMediaFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(MediaYouTube.ARG_SECTIONS, sections)
                }
            }
        }
    }
}
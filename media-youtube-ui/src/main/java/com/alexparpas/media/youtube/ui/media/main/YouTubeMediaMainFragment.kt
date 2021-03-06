package com.alexparpas.media.youtube.ui.media.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexparpas.media.youtube.R
import com.alexparpas.media.youtube.core.model.*
import com.alexparpas.media.youtube.ui.MediaYouTubeUi
import com.alexparpas.media.youtube.ui.common.EmptyState
import com.alexparpas.media.youtube.ui.common.ErrorState
import com.alexparpas.media.youtube.ui.common.LoadingState
import com.alexparpas.media.youtube.ui.common.NormalState
import com.alexparpas.media.youtube.ui.media.adapter.YouTubeMediaOuterAdapter
import com.alexparpas.media.youtube.ui.media.adapter.YouTubeMediaVideosAdapter
import com.alexparpas.media.youtube.ui.media.more.YouTubeMediaMoreFragment
import kotlinx.android.synthetic.main.myt_fragment_youtube_media.*

internal class YouTubeMediaMainFragment : Fragment(), YouTubeMediaVideosAdapter.Callback {

    private lateinit var viewModel: YouTubeMediaMainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.myt_fragment_youtube_media, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = YouTubeMediaOuterAdapter(
                callback = this,
                viewPool = RecyclerView.RecycledViewPool()
        )
        initRecyclerView(adapter)

        observeVideos(adapter)
        observeViewState()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(
                this,
                MediaYouTubeUi.Injection.provideMediaViewModelFactory(
                        sections = requireNotNull(arguments?.getParcelableArrayList(MediaYouTubeUi.ARG_SECTIONS))
                )
        ).get(YouTubeMediaMainViewModel::class.java)
    }

    private fun initRecyclerView(adapter: YouTubeMediaOuterAdapter) {
        recycler_view.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeVideos(adapter: YouTubeMediaOuterAdapter) {
        viewModel.mediaLiveData.observe(viewLifecycleOwner, Observer { videos ->
            if (videos != null) {
                adapter.videos = videos
            }
        })
    }

    private fun observeViewState() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadingState -> setLoadingState()
                NormalState -> setNormalState()
                EmptyState -> setEmptyState()
                is ErrorState -> setErrorState()
            }
        })
    }

    private fun setErrorState() {
        recycler_view.visibility = View.GONE
        loading_pb.visibility = View.GONE
        empty_tv.visibility = View.GONE
        error_tv.visibility = View.VISIBLE
    }

    private fun setNormalState() {
        recycler_view.visibility = View.VISIBLE
        loading_pb.visibility = View.GONE
        empty_tv.visibility = View.GONE
        error_tv.visibility = View.GONE
    }

    private fun setEmptyState(){
        recycler_view.visibility = View.GONE
        loading_pb.visibility = View.GONE
        empty_tv.visibility = View.VISIBLE
        error_tv.visibility = View.GONE
    }

    private fun setLoadingState() {
        recycler_view.visibility = View.GONE
        loading_pb.visibility = View.VISIBLE
        empty_tv.visibility = View.GONE
        error_tv.visibility = View.GONE
    }

    override fun onVideoClicked(videoId: String) {
        MediaYouTubeUi.playVideo(requireContext(), videoId)
    }

    override fun onCategoryClicked(category: CategoryItem) {
        YouTubeMediaMoreFragment.newInstance(
                category.title,
                ArrayList(category.videoIds)
        ).show(childFragmentManager, "frag")
    }

    companion object {
        fun newInstance(sections: ArrayList<VideoSection>): YouTubeMediaMainFragment {
            return YouTubeMediaMainFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(MediaYouTubeUi.ARG_SECTIONS, sections)
                }
            }
        }
    }
}
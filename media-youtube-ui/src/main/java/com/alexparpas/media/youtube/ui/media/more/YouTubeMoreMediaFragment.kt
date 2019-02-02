package com.alexparpas.media.youtube.ui.media.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.alexparpas.media.youtube.ui.MediaYouTubeUi
import com.alexparpas.media.youtube.R
import com.alexparpas.media.youtube.core.CategoryItem
import com.alexparpas.media.youtube.ui.media.main.adapter.YouTubeMediaVideosAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_youtube_more_media.*

class YouTubeMoreMediaFragment : BottomSheetDialogFragment(), YouTubeMediaVideosAdapter.Callback {
    private lateinit var viewModel: YouTubeMoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_youtube_more_media, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = YouTubeMediaVideosAdapter(this)

        initRecyclerView(adapter)
        observeCategoryName()
        observeVideos(adapter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(
                this,
                MediaYouTubeUi.Injection.provideMoreViewModelFactory(
                        videoIds = requireNotNull(arguments?.getStringArrayList(ARG_VIDEO_IDS)),
                        categoryName = requireNotNull(arguments?.getString(ARG_CATEGORY_NAME))
                )
        ).get(YouTubeMoreViewModel::class.java)
    }

    private fun initRecyclerView(adapter: YouTubeMediaVideosAdapter) {
        recycler_view.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun observeCategoryName() {
        viewModel.categoryNameLiveData.observe(viewLifecycleOwner, Observer {
            category_tv.text = it
        })
    }

    private fun observeVideos(adapter: YouTubeMediaVideosAdapter) {
        viewModel.videosLiveData.observe(viewLifecycleOwner, Observer {
            adapter.videos = it
        })
    }

    override fun onVideoClicked(videoId: String) {
        MediaYouTubeUi.playVideo(requireContext(), videoId)
    }

    override fun onCategoryClicked(category: CategoryItem) {
        //no-op
    }

    companion object {
        private const val ARG_VIDEO_IDS = "ARG_VIDEO_IDS"
        private const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"

        fun newInstance(categoryName: String, videoIds: ArrayList<String>) = YouTubeMoreMediaFragment().apply {
            arguments = Bundle().apply {
                putStringArrayList(ARG_VIDEO_IDS, videoIds)
                putString(ARG_CATEGORY_NAME, categoryName)
            }
        }
    }
}
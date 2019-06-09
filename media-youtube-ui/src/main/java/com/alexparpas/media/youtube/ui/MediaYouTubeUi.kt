package com.alexparpas.media.youtube.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.alexparpas.media.youtube.core.MediaYouTube
import com.alexparpas.media.youtube.core.model.VideoSection
import com.alexparpas.media.youtube.ui.media.main.YouTubeMediaMainFragment
import com.alexparpas.media.youtube.ui.media.main.YouTubeMediaViewModelFactory
import com.alexparpas.media.youtube.ui.media.more.YouTubeMoreViewModelFactory
import com.alexparpas.media.youtube.ui.video.VideoActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

object MediaYouTubeUi {
    private val videoPlayedSubject: PublishSubject<String> = PublishSubject.create()
    internal const val ARG_SECTIONS = "ARG_SECTIONS"
    internal const val ARG_VIDEO_ID = "ARG_VIDEO_ID"

    fun getMainMediaFragment(sections: ArrayList<VideoSection>): Fragment =
            YouTubeMediaMainFragment.newInstance(sections)

    fun playVideo(context: Context, videoId: String) {
        context.startActivity(
                Intent(context, VideoActivity::class.java).apply {
                    putExtra(ARG_VIDEO_ID, videoId)
                }
        )
        videoPlayedSubject.onNext(videoId)
    }

    fun onVideoPlayed(): Observable<String> = videoPlayedSubject

    internal object Injection {
        fun provideMediaViewModelFactory(sections: List<VideoSection>) =
                YouTubeMediaViewModelFactory(
                        sections = sections,
                        ioScheduler = Schedulers.io(),
                        uiScheduler = AndroidSchedulers.mainThread(),
                        repository = MediaYouTube.youTubeMediaRepository
                )

        fun provideMoreViewModelFactory(categoryName: String, videoIds: List<String>) =
                YouTubeMoreViewModelFactory(
                        categoryName,
                        videoIds,
                        ioScheduler = Schedulers.io(),
                        uiScheduler = AndroidSchedulers.mainThread(),
                        repository = MediaYouTube.youTubeMediaRepository
                )
    }
}
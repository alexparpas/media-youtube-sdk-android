package com.alexparpas.media.youtube

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.alexparpas.media.youtube.data.LocalStorage
import com.alexparpas.media.youtube.data.VideoSection
import com.alexparpas.media.youtube.data.YouTubeMediaRepository
import com.alexparpas.media.youtube.data.YouTubeMediaService
import com.alexparpas.media.youtube.ui.media.main.YouTubeMainMediaFragment
import com.alexparpas.media.youtube.ui.media.main.YouTubeMediaViewModelFactory
import com.alexparpas.media.youtube.ui.media.more.YouTubeMoreMediaFragment
import com.alexparpas.media.youtube.ui.media.more.YouTubeMoreViewModelFactory
import com.alexparpas.media.youtube.ui.video.VideoActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MediaYouTube {
    private lateinit var app: Application
    private lateinit var apiKey: String
    internal const val ARG_SECTIONS = "ARG_SECTIONS" //TODO Consider removing from here
    internal const val ARG_VIDEO_ID = "ARG_VIDEO_ID"

    val youTubeMediaRepository by lazy {
        YouTubeMediaRepository(
                apiKey,
                LocalStorage,
                Injection.getYouTubeService()
        )
    }

    fun init(app: Application, apiKey: String) {
        MediaYouTube.app = app
        MediaYouTube.apiKey = apiKey
    }

    fun getMainMediaFragment(sections: ArrayList<VideoSection>): Fragment =
            YouTubeMainMediaFragment.newInstance(sections)

    fun playVideo(context: Context, videoId: String) {
        context.startActivity(
                Intent(context, VideoActivity::class.java).apply {
                    putExtra(ARG_VIDEO_ID, videoId)
                }
        )
    }

    internal object Injection {
        fun provideMediaViewModelFactory(sections: List<VideoSection>) =
                YouTubeMediaViewModelFactory(
                        sections = sections,
                        ioScheduler = Schedulers.io(),
                        uiScheduler = AndroidSchedulers.mainThread(),
                        repository = youTubeMediaRepository
                )

        fun provideMoreViewModelFactory(categoryName: String, videoIds: List<String>) =
                YouTubeMoreViewModelFactory(
                        categoryName,
                        videoIds,
                        ioScheduler = Schedulers.io(),
                        uiScheduler = AndroidSchedulers.mainThread(),
                        repository = youTubeMediaRepository
                )

        fun getYouTubeService() =
                Retrofit.Builder()
                        .baseUrl(app.getString(R.string.youtube_api_base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                        .create(YouTubeMediaService::class.java)!!
    }
}
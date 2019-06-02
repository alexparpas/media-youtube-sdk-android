package com.alexparpas.media.youtube.ui.media.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexparpas.media.youtube.core.model.VideoItem
import com.alexparpas.media.youtube.core.data.YouTubeMediaRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

internal class YouTubeMediaMoreViewModel(
        private val categoryName: String,
        private val videoIds: List<String>,
        private val ioScheduler: Scheduler,
        private val uiScheduler: Scheduler,
        private val repository: YouTubeMediaRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _videosLiveData = MutableLiveData<List<VideoItem>>()
    val categoryNameLiveData = MutableLiveData<String>()
    val videosLiveData: LiveData<List<VideoItem>> = _videosLiveData

    init {
        getVideos()
        categoryNameLiveData.value = categoryName
    }

    private fun getVideos() {
        repository.getVideos(videoIds)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribeBy(
                        onSuccess = { _videosLiveData.value = it },
                        onError = { Timber.e(it) }
                ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

@Suppress("UNCHECKED_CAST")
class YouTubeMoreViewModelFactory(
        private val categoryName: String,
        private val videoIds: List<String>,
        private val ioScheduler: Scheduler,
        private val uiScheduler: Scheduler,
        private val repository: YouTubeMediaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return YouTubeMediaMoreViewModel(
                categoryName,
                videoIds,
                ioScheduler,
                uiScheduler,
                repository
        ) as T
    }
}
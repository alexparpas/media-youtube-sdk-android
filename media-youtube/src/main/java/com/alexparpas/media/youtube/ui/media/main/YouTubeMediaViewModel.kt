package com.alexparpas.media.youtube.ui.media.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexparpas.media.youtube.data.*
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class YouTubeMediaViewModel(
        private val sections: List<VideoSection>,
        private val ioScheduler: Scheduler,
        private val uiScheduler: Scheduler,
        private val repository: YouTubeMediaRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()
    private val _mediaLiveData = MutableLiveData<List<MediaItem>>()
    val mediaLiveData: LiveData<List<MediaItem>> = _mediaLiveData

    init {
        getVideos(sections)
    }

    private fun getVideos(sections: List<VideoSection>) {
        repository.getVideoIds(sections)
                .flatMap {
                    repository.getVideos(it).subscribeOn(ioScheduler)
                }.map { videos -> sections.map { it.toMediaBindingItem(videos) }.flatten() } //Transform to media binding item list
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribeBy(
                        onSuccess = {
                            _mediaLiveData.value = it
                        },
                        onError = {
                            Timber.e(it)
                        }
                ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

@Suppress("UNCHECKED_CAST")
class YouTubeMediaViewModelFactory(
        private val sections: List<VideoSection>,
        private val ioScheduler: Scheduler,
        private val uiScheduler: Scheduler,
        private val repository: YouTubeMediaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return YouTubeMediaViewModel(
                sections,
                ioScheduler,
                uiScheduler,
                repository
        ) as T
    }
}
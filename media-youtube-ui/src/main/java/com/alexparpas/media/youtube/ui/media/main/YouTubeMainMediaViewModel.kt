package com.alexparpas.media.youtube.ui.media.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexparpas.media.youtube.core.*
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class YouTubeMainMediaViewModel(
        sections: List<VideoSection>,
        private val ioScheduler: Scheduler,
        private val uiScheduler: Scheduler,
        private val repository: YouTubeMediaRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()
    private val _mediaLiveData = MutableLiveData<List<MediaItem>>()
    val mediaLiveData: LiveData<List<MediaItem>> = _mediaLiveData
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    init {
        getVideos(sections)
    }

    private fun getVideos(sections: List<VideoSection>) {
        repository.getVideoIds(sections)
                .flatMap {
                    repository.getVideos(it).subscribeOn(ioScheduler)
                }.map { videos -> sections.map { it.toMediaBindingItem(videos) }.flatten() } //Transform to media binding item list
                .doOnSubscribe { _viewState.postValue(LoadingState) }
                .doOnError { _viewState.postValue(ErrorState(it)) }
                .doOnSuccess { _viewState.postValue(NormalState) }
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
        return YouTubeMainMediaViewModel(
                sections,
                ioScheduler,
                uiScheduler,
                repository
        ) as T
    }
}
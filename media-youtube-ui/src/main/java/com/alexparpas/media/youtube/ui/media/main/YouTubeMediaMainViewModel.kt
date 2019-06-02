package com.alexparpas.media.youtube.ui.media.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexparpas.media.youtube.core.data.YouTubeMediaRepository
import com.alexparpas.media.youtube.core.model.MediaItem
import com.alexparpas.media.youtube.core.model.VideoSection
import com.alexparpas.media.youtube.ui.common.*
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

internal class YouTubeMediaMainViewModel(
        private var sections: List<VideoSection>,
        private val ioScheduler: Scheduler,
        private val uiScheduler: Scheduler,
        private val repository: YouTubeMediaRepository
) : ViewModel() {
    private val _mediaLiveData = MutableLiveData<List<MediaItem>>()
    val mediaLiveData: LiveData<List<MediaItem>> = _mediaLiveData

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val disposables = CompositeDisposable()

    init {
        getVideos()
    }

    private fun getVideos() {
        repository.sortByOrder(sections)
                .flatMap {
                    sections = it
                    repository.getVideoIds(sections).subscribeOn(ioScheduler)
                }
                .flatMap {
                    repository.getVideos(sections, it).subscribeOn(ioScheduler)
                }
                .doOnSubscribe { _viewState.postValue(LoadingState) }
                .doOnError { _viewState.postValue(ErrorState(it)) }
                .doOnSuccess {
                    if (it.isEmpty()) _viewState.postValue(EmptyState) else _viewState.postValue(NormalState)
                }
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
        return YouTubeMediaMainViewModel(
                sections,
                ioScheduler,
                uiScheduler,
                repository
        ) as T
    }
}
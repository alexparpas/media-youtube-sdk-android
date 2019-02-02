package com.alexparpas.media.youtube

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object Navigator {
    private val navigationEventSubject = PublishSubject.create<NavigationEvent>()

    fun onNavigationEvent(): Observable<NavigationEvent> {
        return navigationEventSubject
    }

    fun sendNavigationEvent(event: NavigationEvent) {
        navigationEventSubject.onNext(event)
    }
}

sealed class NavigationEvent
object MediaMainEvent : NavigationEvent()
object MediaMoreEvent : NavigationEvent()

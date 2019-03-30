package com.alexparpas.media.youtube.core.model

sealed class ViewState
object LoadingState : ViewState()
object NormalState : ViewState()
class ErrorState(val error: Throwable) : ViewState()
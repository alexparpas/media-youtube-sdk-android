package com.alexparpas.media.youtube.ui.common

internal sealed class ViewState
internal object LoadingState : ViewState()
internal object NormalState : ViewState()
internal object EmptyState : ViewState()
internal class ErrorState(val error: Throwable) : ViewState()
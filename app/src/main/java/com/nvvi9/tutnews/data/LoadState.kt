package com.nvvi9.tutnews.data

sealed class LoadState {

    class NotLoading : LoadState()

    class Loading : LoadState()

    class Error(val message: String?) : LoadState()
}
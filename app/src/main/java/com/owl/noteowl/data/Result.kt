package com.owl.noteowl.data

sealed class Result<T> {
    data class Progress<T>(var isLoading: Boolean) : Result<T>()
    data class Error<T>(var error: Throwable) : Result<T>()
    data class Success<T>(var data: T) : Result<T>()

    companion object {
        fun <T> loading(isLoading: Boolean): Result<T> = Progress(isLoading)
        fun <T> error(throwable: Throwable): Result<T> = Error(throwable)
        fun <T> success(data: T): Result<T> = Success(data)
    }

    fun parseResponse(
        progress: (isLoading: Boolean) -> Unit,
        success: (data: T) -> Unit,
        error: (error: Throwable) -> Unit
    ) {
        when (this) {
            // loading
            is Progress -> {
                progress(this.isLoading)
            }

            //success
            is Success -> {
                success(this.data)
            }

            //error
            is Error -> {
                error(this.error)
            }
        }
    }
}
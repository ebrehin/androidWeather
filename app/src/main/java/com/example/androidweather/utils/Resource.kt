package com.example.androidweather.utils

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

sealed class Resource<T>(
    val status: Status,
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(Status.SUCCESS, data)
    class Loading<T>(data: T? = null) : Resource<T>(Status.LOADING, data)
    class Error<T>(throwable: Throwable, data: T? = null) :
        Resource<T>(Status.ERROR, data, throwable)
} 
package com.example.footballleague.utils.resource

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class EmptyData<T> : Resource<T>()
    class Error<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    class CachedData<T>(data: T?) : Resource<T>(data)
}
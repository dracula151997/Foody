package com.tutorial.foody.data.network

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String? = null, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T>() : NetworkResult<T>()

}

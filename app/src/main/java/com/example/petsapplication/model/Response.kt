package com.example.petsapplication.model

sealed class Response<T>(val data: T? = null,val message: String? = null) {
    class Success<T>(data: T) : Response<T>(data)
    class Failure<T>(data: T? = null, message: String) : Response<T>(data, message)
    class Loading<T>(data: T? = null) : Response<T>(data)
}

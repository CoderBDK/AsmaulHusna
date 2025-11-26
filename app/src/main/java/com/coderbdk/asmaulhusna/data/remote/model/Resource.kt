package com.coderbdk.asmaulhusna.data.remote.model

sealed class Resource<out T> {
    object Loading: Resource<Nothing>()
    data class Success<T>(val data: T): Resource<T>()
    data class Error(val message: String, val code: Int? = null): Resource<Nothing>()
}
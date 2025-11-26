package com.coderbdk.asmaulhusna.data.remote

import com.coderbdk.asmaulhusna.data.remote.model.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class ApiService @Inject constructor(
    @PublishedApi
    internal val client: HttpClient
) {
    suspend inline fun <reified T> get(url: String): Resource<T> {
        return try {
            val response: T = client.get(url).body()
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    suspend inline fun <reified T, reified R> post(url: String, body: T): Resource<R> {
        return try {
            val response: R = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }
}

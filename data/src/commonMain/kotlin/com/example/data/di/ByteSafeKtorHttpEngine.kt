package com.example.data.di

import com.apollographql.apollo.api.http.HttpHeader
import com.apollographql.apollo.api.http.HttpMethod
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.network.http.HttpEngine
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.content.ByteArrayContent
import io.ktor.http.ContentType
import io.ktor.util.flattenEntries
import okio.Buffer
import kotlin.coroutines.cancellation.CancellationException

// Same as apollo-engine-ktor's KtorHttpEngine, except the request body is sent as bytes.
// The library sends it with readUtf8(), which turns every non-text byte of a file upload into U+FFFD,
// so playlist covers reached Supabase as broken images on every platform.
class ByteSafeKtorHttpEngine(
    private val client: HttpClient,
) : HttpEngine {

    private var disposed = false

    override suspend fun execute(request: HttpRequest): HttpResponse {
        try {
            val response = client.request(request.url) {
                method = when (request.method) {
                    HttpMethod.Get -> io.ktor.http.HttpMethod.Get
                    HttpMethod.Post -> io.ktor.http.HttpMethod.Post
                }
                request.headers.forEach {
                    header(it.name, it.value)
                }
                request.body?.let {
                    val buffer = Buffer()
                    it.writeTo(buffer)
                    // The content type travels with the body; Ktor refuses it as a plain header next to one.
                    setBody(ByteArrayContent(buffer.readByteArray(), ContentType.parse(it.contentType)))
                }
            }
            val responseByteArray: ByteArray = response.body()
            return HttpResponse.Builder(statusCode = response.status.value)
                .body(Buffer().write(responseByteArray))
                .addHeaders(response.headers.flattenEntries().map { HttpHeader(it.first, it.second) })
                .build()
        } catch (e: CancellationException) {
            throw e
        } catch (t: Throwable) {
            throw ApolloNetworkException(t.message, t)
        }
    }

    override fun close() {
        if (!disposed) {
            client.close()
            disposed = true
        }
    }
}

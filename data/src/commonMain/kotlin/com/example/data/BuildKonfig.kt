package com.example.data

import com.example.data.generated.BuildKonfig as GeneratedBuildKonfig

/**
 * Public bridge to expose the internal generated BuildKonfig to other modules.
 */
object BuildKonfig {
    val BASE_URL: String get() = GeneratedBuildKonfig.BASE_URL
    val BASE_URL_GRAPHQL: String get() = GeneratedBuildKonfig.BASE_URL_GRAPHQL
}

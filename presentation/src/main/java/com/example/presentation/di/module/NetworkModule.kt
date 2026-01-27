package com.example.presentation.di.module

import android.content.Context
import com.example.data.api.MusicApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.data.BuildConfig
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.data.api.auth.AuthInterceptor
import com.example.data.datasource.local.DataStoreAuthStore
import com.example.data.repository.auth.AuthStore
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideApolloClient(client: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BASE_URL + BuildConfig.BASE_URL_GRAPHQL)
            .okHttpClient(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        authInterceptor: AuthInterceptor // Inject here
    ): OkHttpClient {
        val chucker = ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(chucker)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object DataStoreModule {

        @Provides
        @Singleton
        fun provideAuthStore(
            @ApplicationContext context: Context
        ): AuthStore = DataStoreAuthStore(context)
    }
    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MusicApi {
        return retrofit.create(MusicApi::class.java)
    }
}
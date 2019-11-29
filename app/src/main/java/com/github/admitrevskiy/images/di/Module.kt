package com.github.admitrevskiy.images.di

import com.github.admitrevskiy.images.model.rest.NasaApi
import com.github.admitrevskiy.images.repository.PhotoRepository
import com.github.admitrevskiy.images.repository.PhotoRepositoryImpl
import com.github.admitrevskiy.images.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

private const val TIMEOUT = 60L
private const val BASE_URL = "https://api.nasa.gov/mars-photos/"

val appModule = module {

    single { createOkHttpClient() }
    single { createRetrofit(get()) }
    single { createGitHubApi(get()) }

    single { createImagesRepository(get()) }

    // ViewModel
    viewModel { MainViewModel(get()) }
}

private fun createImagesRepository(api: NasaApi) : PhotoRepository = PhotoRepositoryImpl(api)

/**
 * Creates OkHttpClient
 */
private fun createOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()

/**
 * Creates Retrofit
 */
private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()

/**
 * Creates GitHubApi
 */
private fun createGitHubApi(retrofit: Retrofit): NasaApi = retrofit.create(
    NasaApi::class.java
)
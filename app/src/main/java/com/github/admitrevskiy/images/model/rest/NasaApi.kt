package com.github.admitrevskiy.images.model.rest

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * See <a href=https://api.nasa.gov>doc</a> for more info
 */
interface NasaApi {

    @GET("api/v1/rovers/curiosity/photos")
    fun getMarsPhotos(@Query("sol") sol : Int,
                      @Query("page") page: Int,
                      @Query("api_key") apiKey : String = "DEMO_KEY") : Single<NasaResponse>
}
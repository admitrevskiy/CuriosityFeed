package com.github.admitrevskiy.images.repository

import com.github.admitrevskiy.images.model.rest.NasaApi
import com.github.admitrevskiy.images.model.rest.NasaResponse
import io.reactivex.Single

class PhotoRepositoryImpl(private val api: NasaApi) : PhotoRepository {

    override fun getPhotos(sol: Int, page: Int): Single<NasaResponse> = api.getMarsPhotos(sol, page)

}
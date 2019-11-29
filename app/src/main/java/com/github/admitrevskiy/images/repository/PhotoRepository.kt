package com.github.admitrevskiy.images.repository

import com.github.admitrevskiy.images.model.rest.NasaResponse
import io.reactivex.Single

/**
 * Repository to load photos with
 */
interface PhotoRepository {

     /**
      * @param sol  day on Mars
      * @param page page to load
      * @return Single with {@link NasaResponse}
      */
     fun getPhotos(sol: Int, page: Int) : Single<NasaResponse>
}
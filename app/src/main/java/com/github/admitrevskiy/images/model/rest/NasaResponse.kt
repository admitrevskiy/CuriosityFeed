package com.github.admitrevskiy.images.model.rest

import com.github.admitrevskiy.images.model.Photo
import com.google.gson.annotations.SerializedName

/**
 * Response is a list of photos
 */
class NasaResponse(@SerializedName("photos") val photos : List<Photo>)

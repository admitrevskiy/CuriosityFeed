package com.github.admitrevskiy.images.model

import com.google.gson.annotations.SerializedName

/**
 * Photo meta information
 */
class Photo(

    /**
     * Photo identifier
     */
    @SerializedName("id")
    val id: Int,

    /**
     * Day on Mars
     */
    @SerializedName("sol")
    val sol: Int,

    /**
     * Photo source
     */
    @SerializedName("img_src")
    val source: String,

    /**
     * Date on Earth
     */
    @SerializedName("earth_date")
    val earthDate: String,

    /**
     * Rover which took a photo
     */
    @SerializedName("rover")
    val rover: Rover

) {
    override fun toString(): String {
        return "Photo(id=$id, sol=$sol, source='$source', earthDate='$earthDate', rover=$rover)"
    }
}
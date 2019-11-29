package com.github.admitrevskiy.images.model

import com.google.gson.annotations.SerializedName

/**
 * This class represents Mars rover
 */
class Rover(

    /**
     * Rover identifier
     */
    @SerializedName("id")
    val id: Int,

    /**
     * Day on Mars
     */
    @SerializedName("name")
    val name: String,

    /**
     * Date of landing
     */
    @SerializedName("landing_date")
    val landingDate: String,

    /**
     * Max SOL for this rover
     */
    @SerializedName("max_sol")
    val maxSol: String
)
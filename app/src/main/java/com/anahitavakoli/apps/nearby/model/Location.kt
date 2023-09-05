package com.anahitavakoli.apps.nearby.model

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("x")
    var longitude: Double,
    @SerializedName("y")
    var latitude: Double
)

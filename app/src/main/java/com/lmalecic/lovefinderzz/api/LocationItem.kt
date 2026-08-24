package com.lmalecic.lovefinderzz.api

import com.google.gson.annotations.SerializedName

data class LocationItem(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("dimension") val dimension: String,
    @SerializedName("residents") val residents: List<String>
)

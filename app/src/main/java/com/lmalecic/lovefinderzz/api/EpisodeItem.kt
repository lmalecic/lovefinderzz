package com.lmalecic.lovefinderzz.api

import com.google.gson.annotations.SerializedName

data class EpisodeItem(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("air_date") val airDate: String,
    @SerializedName("episode") val episode: String,
    @SerializedName("characters") val characters: List<String>
)

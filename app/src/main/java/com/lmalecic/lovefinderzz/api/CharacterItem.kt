package com.lmalecic.lovefinderzz.api

import com.google.gson.annotations.SerializedName

data class CharacterItem(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("origin") val origin: CharacterLocationItem,
    @SerializedName("location") val location: CharacterLocationItem,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("episode") val episodes: List<String>,
)

data class CharacterLocationItem(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
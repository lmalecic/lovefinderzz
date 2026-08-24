package com.lmalecic.lovefinderzz.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://rickandmortyapi.com/api/"

sealed interface RickAndMortyApi {

    @GET("character")
    suspend fun fetchCharacters(@Query("page") page: Int? = null) : ApiResponse<CharacterItem>

    @GET("location")
    suspend fun fetchLocations(@Query("page") page: Int? = null) : ApiResponse<LocationItem>

    @GET("episode")
    suspend fun fetchEpisodes(@Query("page") page: Int? = null) : ApiResponse<EpisodeItem>
}
package com.lmalecic.lovefinderzz.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RickAndMortyClient {

    val api: RickAndMortyApi by lazy {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<RickAndMortyApi>()
    }
}
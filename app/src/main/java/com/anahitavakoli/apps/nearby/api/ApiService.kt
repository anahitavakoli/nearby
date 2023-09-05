package com.anahitavakoli.myapplication.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.neshan.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
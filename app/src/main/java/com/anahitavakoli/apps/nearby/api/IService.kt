package com.anahitavakoli.myapplication.api

import com.anahitavakoli.apps.nearby.model.Address
import com.anahitavakoli.apps.nearby.model.PlaceData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IService {

    @GET("v5/reverse")
    fun getPosition(@Header("Api-Key") key : String,@Query("lat") lat : Double, @Query("lng") lng : Double): Call<Address>

    @GET("v1/search")
    fun search(@Header("Api-Key") key : String,@Query("term") term : String,@Query("lat") lat : Double, @Query("lng") lng : Double): Call<PlaceData>

}
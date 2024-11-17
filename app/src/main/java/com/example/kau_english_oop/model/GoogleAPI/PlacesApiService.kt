package com.example.kau_english_oop.model.GoogleAPI

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String, // lat,lng 형식
        @Query("radius") radius: Int, // 검색 반경
        //@Query("type") type: String, // 장소 유형
        @Query("key") apiKey: String // API 키
    ): Response<PlacesResponse>
}
package com.example.kau_english_oop.ViewModel

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kau_english_oop.model.GoogleAPI.PlacesApiService
import com.example.kau_english_oop.model.GoogleAPI.PlacesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapViewModel : ViewModel() {
    private val _placesResponse = MutableLiveData<PlacesResponse>() // PlacesResponse를 저장할 MutableLiveData
    val placesResponse: LiveData<PlacesResponse> get() = _placesResponse // 외부에서 읽기 위한 LiveData

    private val apiKey = "AIzaSyDqRBFBeX0BDgt56Js8OVJJcHHAJbyfUgk" // API 키를 여기에 입력하세요.

    fun fetchNearbyPlaces(location: String, radius: Int) {
        val service = Retrofit.Builder() // retrofit으로 http api 요청
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApiService::class.java) // 주변 검색 구글 api

        // IO 스레드에서 비동기 작업 수행
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 주변 장소 정보 요청
                val response = service.getNearbyPlaces(location, radius, apiKey)
                // 응답이 성공적이고 본문이 null이 아닐 경우
                if (response.isSuccessful && response.body() != null) {
                    _placesResponse.postValue(response.body()) // LiveData에 응답 저장
                } else {
                    Log.e("MapViewModel", "Failed to fetch places. Status code: ${response.code()}") // 오류 로그
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "Error fetching places: ${e.localizedMessage}") // 예외 발생 시 로그
            }
        }
    }
}
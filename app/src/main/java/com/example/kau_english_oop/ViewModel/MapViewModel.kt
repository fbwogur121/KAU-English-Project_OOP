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
    private val _placesResponse = MutableLiveData<PlacesResponse>()
    val placesResponse: LiveData<PlacesResponse> get() = _placesResponse

    private val apiKey = "AIzaSyDqRBFBeX0BDgt56Js8OVJJcHHAJbyfUgk" // API 키를 여기에 입력하세요.

    fun fetchNearbyPlaces(location: String, radius: Int) {
        val service = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getNearbyPlaces(location, radius, apiKey)
                if (response.isSuccessful && response.body() != null) {
                    _placesResponse.postValue(response.body())
                } else {
                    Log.e("MapViewModel", "Failed to fetch places. Status code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "Error fetching places: ${e.localizedMessage}")
            }
        }
    }
}
package com.example.kau_english_oop

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.databinding.FragmentMapBinding
import com.example.kau_english_oop.model.GoogleAPI.PlacesApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class MapFragment : Fragment(), OnMapReadyCallback {

    private var binding: FragmentMapBinding? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234

    private var previousMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

            // 현재 위치 가져오기
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    // 현재 위치에 대한 마커 추가
                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                } ?: run {
                    Toast.makeText(requireContext(), "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // 지도 클릭 리스너
            mMap.setOnMapClickListener { latLng ->
                // 이전 마커 제거
                previousMarker?.remove()
                // 클릭한 위치에 파란색 마커 추가
                previousMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("Clicked Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) // 파란색 마커
                )
                println("Clicked Location: Latitude = ${latLng.latitude}, Longitude = ${latLng.longitude}")
                //fetchPlaceDetails(latLng) // 장소 정보 요청
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }


    private fun fetchPlaceDetails(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            // Geocoder를 사용하여 위도 경도로 주소 정보 가져오기
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 주소가 null 또는 비어있는지 확인
            val address = if (addresses != null && addresses.isNotEmpty()) {
                addresses[0].getAddressLine(0)
            } else {
                "주소 정보를 찾을 수 없습니다."
            }

            // 주소 정보를 Toast로 사용자에게 표시
            Toast.makeText(requireContext(), "주소: $address", Toast.LENGTH_SHORT).show()

            // 클릭한 위치에서 건물 정보 요청
            fetchBuildingDetailsFromPlacesAPI(latLng) // 이 줄을 추가하여 건물 정보 요청

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "위치 정보 요청 중 오류 발생", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchBuildingDetailsFromPlacesAPI(latLng: LatLng) {
        val apiKey = "AIzaSyChaP2A7tRcut2rQVc4Qy4OJiGPVoso_B8"
        val radius = 100 // 검색 반경을 설정합니다.

        // OkHttpClient를 사용해 타임아웃 설정 추가
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS) // 연결 타임아웃 설정
            .readTimeout(15, TimeUnit.SECONDS) // 읽기 타임아웃 설정
            .build()

        // Retrofit 객체 생성 시 OkHttpClient 추가
        val service = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PlacesApiService::class.java)

        val location = "${latLng.latitude},${latLng.longitude}"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getNearbyPlaces(location, radius, apiKey)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val places = response.body()!!.results
                        if (places.isNotEmpty()) {
                            // 여기서 UI 업데이트
                            places.forEach { place ->
                                println("Building Name: ${place.name}")
                                println("Vicinity: ${place.vicinity}")
                            }
                        } else {
                            println("No nearby places found.")
                        }
                    } else {
                        println("Failed to fetch places. Status code: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Error fetching places: ${e.localizedMessage}")
                    Toast.makeText(requireContext(), "장소 정보를 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(mMap)
            } else {
                Toast.makeText(requireContext(), "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

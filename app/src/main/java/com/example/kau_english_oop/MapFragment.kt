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
import okhttp3.*
import java.util.concurrent.TimeUnit
import org.json.JSONObject
import java.io.IOException

@Suppress("DEPRECATION")
class MapFragment : Fragment(), OnMapReadyCallback {

    private var binding: FragmentMapBinding? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private var selectedBuildingName: String? = null // 선택된 빌딩 이름 저장 - 이후 ConversationFragment로 넘길 것.

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

        // 대화 버튼 클릭 리스너 추가
        binding?.conversationButton?.setOnClickListener {
            // ConversationFragment로 전환
            val conversationFragment = ConversationFragment().apply {
                arguments = Bundle().apply {
                    putString("buildingName", selectedBuildingName)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, conversationFragment)
                .addToBackStack(null)
                .commit()
        }
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
                fetchBuildingDetailsFromPlacesAPI(latLng) // 장소 정보 요청
                reverseGeocode(latLng)
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun reverseGeocode(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                }
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Address: ${address.getAddressLine(0)}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "역지오코딩 중 오류가 발생했습니다: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchBuildingDetailsFromPlacesAPI(latLng: LatLng) {
        val apiKey = "AIzaSyDqRBFBeX0BDgt56Js8OVJJcHHAJbyfUgk"
        val radius = 30 // 검색 반경 설정 (단위: 미터)
        val location = "${latLng.latitude},${latLng.longitude}"

        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        val service = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PlacesApiService::class.java)


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getNearbyPlaces(location, radius, apiKey)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()
                        println("API Response: $response")

                        val places = response.body()!!.results
                        if (places.isNotEmpty()) {
                            // 가장 가까운 장소 정보 출력
                            //val nearestPlace = places.first()
                            val nearestPlace = places[1]
                            System.out.println("Nearest Building Name: ${nearestPlace.name}")
                            System.out.println("Vicinity: ${nearestPlace.vicinity}")
                            System.out.println("Place Type: ${nearestPlace.types}")

                            selectedBuildingName = nearestPlace.name
                        } else {
                            System.out.println("No nearby places found.")
                        }
                    } else {
                        System.out.println("Failed to fetch places. Status code: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    System.out.println("Error fetching places: ${e.localizedMessage}")
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





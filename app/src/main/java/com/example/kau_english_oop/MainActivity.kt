package com.example.kau_english_oop

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.Calendar.CalendarFragment
import com.example.kau_english_oop.Camera.CameraFragment
import com.example.kau_english_oop.Like.LikeFragment
import com.example.kau_english_oop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)

        setContentView(binding.root)



        // 초기 화면 설정
        loadFragment(HomeFragment())

        // BottomNavigationView 아이템 선택 리스너 설정
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {

                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_map -> {
                    loadFragment(MapFragment())

                    true
                }
                R.id.nav_camera -> {
                    loadFragment(CameraFragment())

                    true
                }
                R.id.nav_like -> {
                    loadFragment(LikeFragment())

                    true
                }
                R.id.nav_calendar -> {
                    loadFragment(CalendarFragment())

                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
            commit()
        }

    }

}








package com.example.kau_english_oop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.databinding.ActivityMainBinding


import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

}








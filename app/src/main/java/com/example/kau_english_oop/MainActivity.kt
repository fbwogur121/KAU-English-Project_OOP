package com.example.kau_english_oop

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.Calendar.CalendarFragment
import com.example.kau_english_oop.Camera.CameraFragment
import com.example.kau_english_oop.Like.LikeFragment
import com.example.kau_english_oop.Map.MapFragment
import com.example.kau_english_oop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // View Binding을 사용하기 위한 변수 선언
    // binding 객체는 XML 레이아웃 파일의 UI 요소에 접근하기 위해 사용됨
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding 초기화 - layoutInflater를 사용해 XML 파일과 연결
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 외부 저장소 읽기 권한 요청
        // 앱 실행 시 외부 저장소 접근을 위한 권한을 요청
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)

        // activity_main.xml 파일의 root 레이아웃을 화면에 표시
        setContentView(binding.root)



        // 초기 화면 설정
        // 앱 실행 시 처음 표시할 Fragment를 HomeFragment로 설정
        loadFragment(HomeFragment())

        // BottomNavigationView 아이템 선택 리스너 설정
        // 사용자가 하단 네비게이션 메뉴를 클릭할 때 실행되는 리스너
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) { // 선택된 메뉴 아이템의 ID에 따라 처리
                R.id.nav_home -> {
                    // HomeFragment를 화면에 로드
                    loadFragment(HomeFragment())
                    true // 클릭 이벤트 처리 완료 표시
                }
                R.id.nav_map -> {
                    // MapFragment를 화면에 로드
                    loadFragment(MapFragment())
                    true
                }
                R.id.nav_camera -> {
                    // CameraFragment를 화면에 로드
                    loadFragment(CameraFragment())
                    true
                }
                R.id.nav_like -> {
                    // LikeFragment를 화면에 로드
                    loadFragment(LikeFragment())
                    true
                }
                R.id.nav_calendar -> {
                    // CalendarFragment를 화면에 로드
                    loadFragment(CalendarFragment())
                    true
                }
                else -> false // 클릭 이벤트가 처리되지 않음
            }
        }
    }

    // Fragment 전환을 위한 함수
    private fun loadFragment(fragment: Fragment) {
        // FragmentManager를 사용해 현재 화면을 선택한 Fragment로 교체
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragmentContainer, fragment) // fragmentContainer에 새로운 Fragment를 교체
            addToBackStack(null) // 백 스택에 추가, 뒤로 가기 시 이전 Fragment로 돌아갈 수 있음
            commit() // 트랜잭션 적용
        }

    }

}






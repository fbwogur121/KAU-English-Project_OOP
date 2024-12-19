package com.example.kau_english_oop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kau_english_oop.databinding.FragmentCalendarBinding
import com.example.kau_english_oop.Adapter.CalendarPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private lateinit var calendarAdapter: CalendarAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val checkedDates = mutableListOf<String>() // Firestore에서 가져온 체크된 날짜 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // ViewPager 설정
        setupViewPager()

        // TabLayout 설정
        setupTabLayout()

        // 기타 버튼 리스너
        setupButtonListeners()

        return binding.root
    }


    private fun setupButtonListeners() {
        // Check 버튼: 현재 화면 유지
        binding.btnCheck.setOnClickListener {
            Toast.makeText(requireContext(), "현재 화면입니다.", Toast.LENGTH_SHORT).show()
        }

        // Ranking 버튼: RankingFragment로 이동
        binding.btnRanking.setOnClickListener {
            Toast.makeText(requireContext(), "Ranking 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
            // Navigation 로직 추가 가능
        }

        // MyPoint 버튼: MyPointFragment로 이동
        binding.btnMypoint.setOnClickListener {
            Toast.makeText(requireContext(), "MyPoint 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
            // Navigation 로직 추가 가능
        }

        // 2024 버튼: 기본 상태 유지
        binding.btn2024.setOnClickListener {
            Toast.makeText(requireContext(), "2024년 캘린더입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCheckedDates() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userDocRef = firestore.collection("users").document(userId)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("checkedDates")) {
                        val dates = document["checkedDates"] as List<String>
                        checkedDates.clear()
                        checkedDates.addAll(dates.map { it.trim() }) // 공백 제거 후 추가
                        updateCalendar()
                    } else {
                        Log.d("CalendarFragment", "checkedDates 필드가 없습니다.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("CalendarFragment", "Firestore 데이터를 불러오지 못했습니다.", exception)
                }
        } else {
            Log.e("CalendarFragment", "로그인된 유저가 없습니다.")
        }
    }


    private fun updateCalendar() {
        // 2024년의 모든 날짜를 하나의 리스트로 변환
        val dates = generateDatesFor2024().flatten() // List<List<String>> -> List<String>
        Log.d("CalendarFragment", "Generated dates: $dates")
        calendarAdapter.submitList(dates) // 올바른 타입 전달
    }

    private fun generateDatesFor2024(): List<List<String>> {
        val months = mutableListOf<List<String>>()
        val calendar = java.util.Calendar.getInstance()

        for (month in 0..11) { // 0 = 1월, 11 = 12월
            val dates = mutableListOf<String>()
            calendar.set(2024, month, 1) // 각 월의 첫 날 설정

            while (calendar.get(java.util.Calendar.MONTH) == month) {
                val formattedDate = java.text.SimpleDateFormat(
                    "yyyy-MM-dd",
                    java.util.Locale.getDefault()
                ).format(calendar.time)
                dates.add(formattedDate)
                calendar.add(java.util.Calendar.DAY_OF_MONTH, 1) // 하루씩 증가
            }

            months.add(dates) // 각 월의 날짜 리스트를 추가
        }

        return months // 월별 날짜 데이터를 담은 리스트 반환
    }


    private fun setupViewPager() {
        val months = generateDatesFor2024()
        val pagerAdapter = CalendarPagerAdapter(months, checkedDates) { date ->
            if (checkedDates.contains(date)) {
                checkedDates.remove(date)
            } else {
                checkedDates.add(date)
            }
            saveCheckedDatesToFirestore()
            binding.viewPagerCalendar.adapter?.notifyDataSetChanged()
        }
        binding.viewPagerCalendar.adapter = pagerAdapter
    }


    private fun setupTabLayout() {
        val tabTitles = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPagerCalendar) { tab, position ->
            tab.text = tabTitles[position] // 각 탭의 제목 설정
            tab.contentDescription = "Tab for ${tabTitles[position]}" // 접근성 설명 추가
        }.attach()
    }




    private fun onDateClicked(date: String) {
        if (checkedDates.contains(date)) {
            // 날짜가 이미 체크된 경우 -> 체크 해제
            checkedDates.remove(date)
        } else {
            // 날짜가 체크되지 않은 경우 -> 체크 추가
            checkedDates.add(date)
        }

        // Firestore 업데이트
        saveCheckedDatesToFirestore()

        // 캘린더 업데이트
        calendarAdapter.notifyDataSetChanged()
    }

    private fun saveCheckedDatesToFirestore() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userDocRef = firestore.collection("users").document(userId)

            // 체크된 날짜의 개수를 포인트로 계산
            val points = checkedDates.size

            // Firestore에 `checkedDates`와 `point` 필드를 업데이트
            val updates = mapOf(
                "checkedDates" to checkedDates,
                "point" to points
            )

            userDocRef.update(updates)
                .addOnSuccessListener {
                    Log.d("CalendarFragment", "Firestore 업데이트 성공: $points points")
                }
                .addOnFailureListener { exception ->
                    Log.e("CalendarFragment", "Firestore 업데이트 실패", exception)
                }
        } else {
            Log.e("CalendarFragment", "로그인된 유저가 없습니다.")
        }
    }

}

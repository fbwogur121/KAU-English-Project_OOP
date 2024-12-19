// CalendarFragment.kt
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
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val selectedDates = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        setupViewPager()
        setupTabLayout()
        setupButtonListeners()

        return binding.root
    }

    private fun setupButtonListeners() {
        binding.btnCheck.setOnClickListener {
            Toast.makeText(requireContext(), "현재 화면입니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnRanking.setOnClickListener {
            Toast.makeText(requireContext(), "Ranking 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnMypoint.setOnClickListener {
            Toast.makeText(requireContext(), "MyPoint 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btn2024.setOnClickListener {
            Toast.makeText(requireContext(), "2024년 캘린더입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchSelectedDates() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userDocRef = firestore.collection("users").document(userId)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("checkedDates")) {
                        val dates = document["checkedDates"] as List<String>
                        selectedDates.clear()
                        selectedDates.addAll(dates.map { it.trim() })
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
        val dates = generateDatesFor2024().flatten()
        Log.d("CalendarFragment", "Generated dates: $dates")
        calendarAdapter.updateDates(dates) // 수정: submitList -> updateDates
    }

    private fun generateDatesFor2024(): List<List<String>> {
        val months = mutableListOf<List<String>>()
        val calendar = java.util.Calendar.getInstance()

        for (month in 0..11) {
            val dates = mutableListOf<String>()
            calendar.set(2024, month, 1)

            while (calendar.get(java.util.Calendar.MONTH) == month) {
                val formattedDate = java.text.SimpleDateFormat(
                    "yyyy-MM-dd",
                    java.util.Locale.getDefault()
                ).format(calendar.time)
                dates.add(formattedDate)
                calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
            }

            months.add(dates)
        }

        return months
    }

    private fun setupViewPager() {
        val months = generateDatesFor2024()
        val pagerAdapter = CalendarPagerAdapter(months, selectedDates) { date ->
            if (selectedDates.contains(date)) {
                selectedDates.remove(date)
            } else {
                selectedDates.add(date)
            }
            saveSelectedDatesToFirestore()
            binding.viewPagerCalendar.adapter?.notifyDataSetChanged()
        }
        binding.viewPagerCalendar.adapter = pagerAdapter
    }

    private fun setupTabLayout() {
        val tabTitles = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPagerCalendar) { tab, position ->
            tab.text = tabTitles[position]
            tab.contentDescription = "Tab for ${tabTitles[position]}"
        }.attach()
    }

    private fun onDateClicked(date: String) {
        if (selectedDates.contains(date)) {
            selectedDates.remove(date)
        } else {
            selectedDates.add(date)
        }

        saveSelectedDatesToFirestore()
        calendarAdapter.notifyDataSetChanged()
    }

    private fun saveSelectedDatesToFirestore() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userDocRef = firestore.collection("users").document(userId)

            val points = selectedDates.size

            val updates = mapOf(
                "checkedDates" to selectedDates,
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

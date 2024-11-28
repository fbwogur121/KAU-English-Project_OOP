package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kau_english_oop.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data Binding 초기화
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this // Data Binding 사용 시 Lifecycle 연결 (옵션)

        // 버튼 클릭 이벤트 초기화
        setupButtonListeners()

        // 캘린더 초기화
        setupCalendar()

        return binding.root
    }

    private fun setupButtonListeners() {
        // Check 버튼: 아무 동작 없음 (현재 화면 유지)
        binding.btnCheck.setOnClickListener {
            Toast.makeText(requireContext(), "현재 화면입니다.", Toast.LENGTH_SHORT).show()
        }

        // 2024 버튼: 기본 상태 유지
        binding.btn2024.setOnClickListener {
            Toast.makeText(requireContext(), "2024년 캘린더입니다.", Toast.LENGTH_SHORT).show()
        }

        // Ranking 버튼: RankingFragment로 이동
        binding.btnRanking.setOnClickListener {
            findNavController().navigate(R.id.action_calendarFragment_to_rankingFragment)
        }

        // MyPoint 버튼: MyPointFragment로 이동
        binding.btnMypoint.setOnClickListener {
            findNavController().navigate(R.id.action_calendarFragment_to_myPointFragment)
        }
    }

    private fun setupCalendar() {
        // RecyclerView를 사용한 캘린더 기본 초기화 (예: 날짜 리스트 표시)
        val calendarAdapter = CalendarAdapter()
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7) // 7일 기준
        binding.calendarRecyclerView.adapter = calendarAdapter

        // 2024년 기본 날짜 데이터 로드 (더미 데이터 추가 가능)
        calendarAdapter.submitList(get2024Dates())
    }

    private fun get2024Dates(): List<String> {
        // 2024년 1월 1일부터 12월 31일까지 날짜 생성 (임시)
        val dates = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        calendar.set(2024, 0, 1) // 2024년 1월 1일
        while (calendar.get(Calendar.YEAR) == 2024) {
            dates.add(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dates
    }
}

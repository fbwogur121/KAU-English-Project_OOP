package com.example.kau_english_oop.Calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.MenuFragment
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentCalendarBinding



class CalendarFragment : Fragment() {
    private var binding: FragmentCalendarBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCalendarBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // btnMenu 클릭 리스너 추가
        binding?.btnMenu?.setOnClickListener {
            // MenuFragment로 전환
            val menuFragment = MenuFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, menuFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
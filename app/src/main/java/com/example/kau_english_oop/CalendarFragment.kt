package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
}
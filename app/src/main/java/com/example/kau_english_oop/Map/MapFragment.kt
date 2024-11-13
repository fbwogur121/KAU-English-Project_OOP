package com.example.kau_english_oop.Map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.databinding.FragmentMapBinding


class MapFragment : Fragment() {
    private var binding: FragmentMapBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMapBinding.inflate(inflater,container,false)
        return binding?.root
    }
}
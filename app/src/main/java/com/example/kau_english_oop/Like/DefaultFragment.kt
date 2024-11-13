package com.example.kau_english_oop.Like

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentDefaultBinding


class DefaultFragment : Fragment() {
    private var binding:FragmentDefaultBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentDefaultBinding.inflate(inflater,container,false)
       return binding?.root

    }


}
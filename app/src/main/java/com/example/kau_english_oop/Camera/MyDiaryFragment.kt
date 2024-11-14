package com.example.kau_english_oop.Camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentMyDiaryBinding


class MyDiaryFragment : Fragment() {

    private var binding: FragmentMyDiaryBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMyDiaryBinding.inflate(inflater,container,false)

        binding?.recentlyButton?.setOnClickListener {

            navigateToFragment(DetailViewFragment())
        }

        binding?.thisYearButton?.setOnClickListener {

            navigateToFragment(DetailViewFragment())
        }

        binding?.timeArrangeButton?.setOnClickListener {

            navigateToFragment(DetailViewFragment())
        }

        return binding?.root
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

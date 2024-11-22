package com.example.kau_english_oop.Like

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentFinanceBinding
import com.example.kau_english_oop.databinding.FragmentScienceBinding


class ScienceFragment : Fragment() {
    private var binding: FragmentScienceBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentScienceBinding.inflate(inflater,container,false)

        binding?.scienceJournal?.setOnClickListener{
            navigateToFragment(ScienceJournalFragment())
        }



        return binding?.root
    }
    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
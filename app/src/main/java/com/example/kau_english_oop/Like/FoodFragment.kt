package com.example.kau_english_oop.Like

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentFinanceBinding
import com.example.kau_english_oop.databinding.FragmentFoodBinding
import com.example.kau_english_oop.databinding.FragmentFoodRecipeBinding


class FoodFragment : Fragment() {
    private var binding: FragmentFoodBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFoodBinding.inflate(inflater,container,false)

        binding?.foodRecipe?.setOnClickListener{
            navigateToFragment(FoodRecipeFragment())
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
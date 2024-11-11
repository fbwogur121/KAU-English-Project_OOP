package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import com.example.kau_english_oop.databinding.FragmentLikeBinding




class LikeFragment : Fragment() {
    private var binding: FragmentLikeBinding? = null
    private val selectedButtons = mutableListOf<String>()
    private val viewModel: LikeResultViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikeBinding.inflate(inflater, container, false)
        val view = binding?.root

        val buttons = listOf(
            binding?.btnTravel to Pair(R.drawable.travel_button_selected, R.drawable.travel_button_unselected),
            binding?.btnScience to Pair(R.drawable.science_button_selected, R.drawable.science_button_unselected),
            binding?.btnFinance to Pair(R.drawable.finance_button_selected, R.drawable.finance_button_unselected),
            binding?.btnAnimal to Pair(R.drawable.animal_button_selected, R.drawable.animal_button_unselected),
            binding?.btnMusic to Pair(R.drawable.music_button_selected, R.drawable.music_button_unselected),
            binding?.btnFood to Pair(R.drawable.food_button_selected, R.drawable.food_button_unselected),
            binding?.btnBeauty to Pair(R.drawable.beauty_button_selected, R.drawable.beauty_button_unselected),
            binding?.btnBook to Pair(R.drawable.book_button_selected, R.drawable.book_button_unselected),
            binding?.btnCloth to Pair(R.drawable.cloth_button_selected, R.drawable.cloth_button_unselected)
        )

        buttons.forEach { (button, drawables) ->
            button?.setOnClickListener {
                val isSelected = it.tag as? Boolean ?: false
                it.tag = !isSelected
                if (!isSelected) {
                    button.setImageResource(drawables.first)
                } else {
                    button.setImageResource(drawables.second)
                }
            }
        }

        binding?.nextButton?.setOnClickListener {
            val selectedDrawables = buttons.filter { (button, _) ->
                button?.tag == true // 선택된 상태만 필터링
            }.map { (_, drawables) ->
                drawables.first // 선택된 상태의 drawable 리소스 ID만 가져오기
            }

            viewModel.setSelectedDrawables(selectedDrawables)
            findNavController().navigate(R.id.action_likeFragment_to_likeResultFragment)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

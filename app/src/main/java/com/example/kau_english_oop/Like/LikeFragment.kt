package com.example.kau_english_oop.Like

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentLikeBinding
import com.example.kau_english_oop.model.ButtonState
import java.util.ArrayList


class LikeFragment : Fragment() {

    private var _binding: FragmentLikeBinding? = null
    private val binding get() = _binding!!
    private val selectedButtons = mutableListOf<String>() // 선택된 버튼의 이름을 저장

    private lateinit var buttonStates: List<ButtonState>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikeBinding.inflate(inflater, container, false)

        // 각 버튼의 상태를 초기화
        buttonStates = listOf(
            ButtonState(binding.btnTravel, R.drawable.travel_button_selected, R.drawable.travel_button_unselected),
            ButtonState(binding.btnScience, R.drawable.science_button_selected, R.drawable.science_button_unselected),
            ButtonState(binding.btnFinance, R.drawable.finance_button_selected, R.drawable.finance_button_unselected),
            ButtonState(binding.btnAnimal, R.drawable.animal_button_selected, R.drawable.animal_button_unselected),
            ButtonState(binding.btnMusic, R.drawable.music_button_selected, R.drawable.music_button_unselected),
            ButtonState(binding.btnFood, R.drawable.food_button_selected, R.drawable.food_button_unselected),
            ButtonState(binding.btnBeauty, R.drawable.beauty_button_selected, R.drawable.beauty_button_unselected),
            ButtonState(binding.btnBook, R.drawable.book_button_selected, R.drawable.book_button_unselected),
            ButtonState(binding.btnCloth, R.drawable.cloth_button_selected, R.drawable.cloth_button_unselected)
        )

        // 각 버튼에 클릭 리스너 설정
        buttonStates.forEach { buttonState ->
            buttonState.button.setOnClickListener {
                toggleButtonSelection(buttonState)
            }
        }

        binding.nextButton.setOnClickListener {
            if (selectedButtons.isEmpty()) {
                Toast.makeText(requireContext(), "하나 이상의 버튼을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val selectedButtonsFragment = SelectedButtonsFragment()
                val bundle = Bundle()
                bundle.putStringArrayList("selectedButtons", ArrayList(selectedButtons))
                selectedButtonsFragment.arguments = bundle

                // FragmentTransaction을 사용하여 Fragment 교체
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, selectedButtonsFragment) // ID 확인
                    .addToBackStack(null) // 뒤로 가기 기능을 위해 백스택에 추가
                    .commit()
            }
        }


        return binding.root
    }

    private fun toggleButtonSelection(buttonState: ButtonState) {
        buttonState.isSelected = !buttonState.isSelected // 선택 상태 토글
        if (buttonState.isSelected) {
            buttonState.button.setImageResource(buttonState.selectedDrawable)
            selectedButtons.add(buttonState.button.contentDescription.toString()) // 버튼의 고유 식별자로 contentDescription 사용
        } else {
            buttonState.button.setImageResource(buttonState.unselectedDrawable)
            selectedButtons.remove(buttonState.button.contentDescription.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.kau_english_oop.Like

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.kau_english_oop.MenuFragment
import com.example.kau_english_oop.R
import com.example.kau_english_oop.ViewModel.LikeViewModel
import com.example.kau_english_oop.databinding.FragmentLikeBinding

// 사용자가 버튼을 선택하고 그 선택한 데이터를 Firestore에 저장하는 Fragment
class LikeFragment : Fragment() {

    private var binding: FragmentLikeBinding? = null

    private val viewModel: LikeViewModel by viewModels()

    private val selectedButtons = mutableListOf<String>() // 선택된 버튼의 이름을 저장

    private lateinit var buttonStates: List<ButtonState>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding 초기화
        binding = FragmentLikeBinding.inflate(inflater, container, false)


        // 각 버튼의 상태를 초기화
        buttonStates = listOf(
            ButtonState(binding?.btnTravel, R.drawable.travel_button_selected, R.drawable.travel_button_unselected),
            ButtonState(binding?.btnScience, R.drawable.science_button_selected, R.drawable.science_button_unselected),
            ButtonState(binding?.btnFinance, R.drawable.finance_button_selected, R.drawable.finance_button_unselected),
            ButtonState(binding?.btnAnimal, R.drawable.animal_button_selected, R.drawable.animal_button_unselected),
            ButtonState(binding?.btnMusic, R.drawable.music_button_selected, R.drawable.music_button_unselected),
            ButtonState(binding?.btnFood, R.drawable.food_button_selected, R.drawable.food_button_unselected),
            ButtonState(binding?.btnBeauty, R.drawable.beauty_button_selected, R.drawable.beauty_button_unselected),
            ButtonState(binding?.btnBook, R.drawable.book_button_selected, R.drawable.book_button_unselected),
            ButtonState(binding?.btnCloth, R.drawable.cloth_button_selected, R.drawable.cloth_button_unselected)
        )

        // 각 버튼에 클릭 리스너 설정
        buttonStates.forEach { buttonState ->
            buttonState.button?.setOnClickListener {
                toggleButtonSelection(buttonState)
            }
        }

        // btnMenu 클릭 리스너 추가
        binding?.btnMenu?.setOnClickListener {
            // MenuFragment로 전환
            val menuFragment = MenuFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, menuFragment)
                .addToBackStack(null)
                .commit()
        }

        // 저장 성공 메시지 관찰
        viewModel.uploadStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "저장 성공!", Toast.LENGTH_SHORT).show()
            }
        }

        // 저장 실패 메시지 관찰
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        // 저장 버튼 클릭 이벤트
        binding?.nextButton?.setOnClickListener {
            if (buttonStates.none { it.isSelected }) {  // 선택된 버튼이 없는 경우
                Toast.makeText(requireContext(), "하나 이상의 버튼을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.saveToFirestore(buttonStates)  // Firestore에 버튼 상태 저장
                navigateToFragment(SelectedButtonsFragment()) // 다음 Fragment로 이동

            }
        }


        return binding?.root  // 생성된 뷰 반환
    }

    private fun toggleButtonSelection(buttonState: ButtonState) {
        // 버튼 선택 상태를 토글 (현재 상태 반전)
        buttonState.isSelected = !buttonState.isSelected // 선택 상태 토글
        if (buttonState.isSelected) {
            // 선택된 상태라면:
            // 1. 버튼 이미지를 선택된 상태 이미지로 변경
            buttonState.button?.setImageResource(buttonState.selectedDrawable)
            // 2. 버튼의 고유 식별자인 contentDescription 값을 리스트에 추가
            selectedButtons.add(buttonState.button?.contentDescription.toString()) // 버튼의 고유 식별자로 contentDescription 사용
        } else {
            // 선택되지 않은 상태라면:
            // 1. 버튼 이미지를 기본(비선택) 상태 이미지로 변경
            buttonState.button?.setImageResource(buttonState.unselectedDrawable)
            // 2. 버튼의 contentDescription 값을 리스트에서 제거
            selectedButtons.remove(buttonState.button?.contentDescription.toString())
        }
    }
    // Fragment 전환 함수
    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ViewBinding 해제
    }
}

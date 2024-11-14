package com.example.kau_english_oop.Like


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentSelectedButtonsBinding

class SelectedButtonsFragment : Fragment() {

    private var _binding: FragmentSelectedButtonsBinding? = null
    private val binding get() = _binding!!
    // 모든 버튼을 리스트에 저장하여 반복적으로 접근
    private lateinit var buttonList: List<Pair<ImageButton, Int>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedButtonsBinding.inflate(inflater, container, false)

        // 전달된 선택된 버튼 리스트 가져오기
        val selectedButtons = arguments?.getStringArrayList("selectedButtons") ?: arrayListOf()

        // 선택된 버튼들에 따라 visibility 설정
        updateButtonVisibility(selectedButtons)
        // 각 버튼과 기본 이미지 리소스를 리스트에 추가
        buttonList = listOf(
            binding.selectedButtonTravel to R.drawable.travel_button_unselected,
            binding.selectedButtonScience to R.drawable.science_button_unselected,
            binding.selectedButtonFinance to R.drawable.finance_button_unselected,
            binding.selectedButtonAnimal to R.drawable.animal_button_unselected,
            binding.selectedButtonMusic to R.drawable.music_button_unselected,
            binding.selectedButtonFood to R.drawable.food_button_unselected,
            binding.selectedButtonBeauty to R.drawable.beauty_button_unselected,
            binding.selectedButtonBook to R.drawable.book_button_unselected,
            binding.selectedButtonCloth to R.drawable.cloth_button_unselected
        )

        // 각 버튼에 공통 클릭 리스너 설정
        buttonList.forEach { (button, _) ->
            button.setOnClickListener { onButtonClicked(button) }
        }

        return binding.root
    }

    private fun updateButtonVisibility(selectedButtons: ArrayList<String>) {
        // 모든 버튼을 숨김 상태로 설정
        binding.selectedButtonTravel.visibility = View.GONE
        binding.selectedButtonScience.visibility = View.GONE
        binding.selectedButtonFinance.visibility = View.GONE
        binding.selectedButtonAnimal.visibility = View.GONE
        binding.selectedButtonMusic.visibility = View.GONE
        binding.selectedButtonFood.visibility = View.GONE
        binding.selectedButtonBeauty.visibility = View.GONE
        binding.selectedButtonBook.visibility = View.GONE
        binding.selectedButtonCloth.visibility = View.GONE

        // 선택된 버튼에 대해 visibility를 VISIBLE로 설정하고 클릭 리스너 추가
        selectedButtons.forEach { buttonDescription ->
            when (buttonDescription) {
                "Travel" -> {
                    binding.selectedButtonTravel.visibility = View.VISIBLE

                }
                "Science" -> {
                    binding.selectedButtonScience.visibility = View.VISIBLE

                }
                "Finance" -> {
                    binding.selectedButtonFinance.visibility = View.VISIBLE

                }
                "Animal" -> {
                    binding.selectedButtonAnimal.visibility = View.VISIBLE

                }
                "Music" -> {
                    binding.selectedButtonMusic.visibility = View.VISIBLE

                }
                "Food" -> {
                    binding.selectedButtonFood.visibility = View.VISIBLE

                }
                "Beauty" -> {
                    binding.selectedButtonBeauty.visibility = View.VISIBLE

                }
                "Book" -> {
                    binding.selectedButtonBook.visibility = View.VISIBLE

                }
                "Cloth" -> {
                    binding.selectedButtonCloth.visibility = View.VISIBLE

                }
            }
        }
    }


    // 버튼 클릭 시 호출되는 공통 처리 함수
    private fun onButtonClicked(selectedButton: ImageButton) {
        // 선택된 버튼에 따른 Fragment 결정
        val fragment = when (selectedButton) {
            binding.selectedButtonTravel -> DefaultFragment()
            binding.selectedButtonScience -> ScienceFragment()
            binding.selectedButtonFinance -> FinanceFragment()
            binding.selectedButtonAnimal -> DefaultFragment()
            binding.selectedButtonMusic -> DefaultFragment()
            binding.selectedButtonFood -> FoodFragment()
            binding.selectedButtonBeauty -> DefaultFragment()
            binding.selectedButtonBook -> DefaultFragment()
            binding.selectedButtonCloth -> DefaultFragment()
            else -> null
        }

        // 모든 버튼의 이미지를 기본 상태로 리셋
        buttonList.forEach { (button, defaultDrawable) ->
            button.setImageResource(defaultDrawable)
        }

        // 선택된 버튼만 선택 상태 이미지로 설정
        selectedButton.setImageResource(
            when (selectedButton) {
                binding.selectedButtonTravel -> R.drawable.travel_button_selected
                binding.selectedButtonScience -> R.drawable.science_button_selected
                binding.selectedButtonFinance -> R.drawable.finance_button_selected
                binding.selectedButtonAnimal -> R.drawable.animal_button_selected
                binding.selectedButtonMusic -> R.drawable.music_button_selected
                binding.selectedButtonFood -> R.drawable.food_button_selected
                binding.selectedButtonBeauty -> R.drawable.beauty_button_unselected
                binding.selectedButtonBook -> R.drawable.book_button_selected
                binding.selectedButtonCloth -> R.drawable.cloth_button_selected
                else -> R.drawable.default_button_image
            }
        )

        // 선택된 Fragment로 이동
        fragment?.let { navigateToFragment(it) }
    }

    private fun navigateToFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction() // 자식 프래그먼트 관리자를 사용하여 교체
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
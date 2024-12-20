package com.example.kau_english_oop.Like


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import com.example.kau_english_oop.R
import com.example.kau_english_oop.ViewModel.LikeViewModel
import com.example.kau_english_oop.databinding.FragmentSelectedButtonsBinding

// 사용자가 선택한 버튼을 표시하고 상호작용할 수 있는 Fragment
class SelectedButtonsFragment : Fragment() {


    private var binding: FragmentSelectedButtonsBinding? = null // ViewBinding 객체

    // LikeViewModel을 activityViewModels로 가져오기 (Activity와 viewModel 공유)
    private val viewModel: LikeViewModel by activityViewModels()

    private lateinit var buttonList: List<Pair<ImageButton?, Int>> // 버튼 리스트와 기본 이미지 쌍

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding 초기화
        binding = FragmentSelectedButtonsBinding.inflate(inflater, container, false)

        // 버튼과 기본 이미지 리소스를 리스트로 저장
        buttonList = listOf(
            binding?.selectedButtonTravel to R.drawable.travel_button_unselected,
            binding?.selectedButtonScience to R.drawable.science_button_unselected,
            binding?.selectedButtonFinance to R.drawable.finance_button_unselected,
            binding?.selectedButtonAnimal to R.drawable.animal_button_unselected,
            binding?.selectedButtonMusic to R.drawable.music_button_unselected,
            binding?.selectedButtonFood to R.drawable.food_button_unselected,
            binding?.selectedButtonBeauty to R.drawable.beauty_button_unselected,
            binding?.selectedButtonBook to R.drawable.book_button_unselected,
            binding?.selectedButtonCloth to R.drawable.cloth_button_unselected
        )

        // Firestore에서 버튼 상태 가져오기
        viewModel.fetchButtonStatesFromFirestore()

        // ViewModel의 LiveData를 관찰하여 UI 업데이트
        viewModel.buttonStatesMap.observe(viewLifecycleOwner) { buttonStates ->
            updateButtonStates(buttonStates) // 버튼 상태를 기반으로 UI 업데이트
        }


        // 각 버튼에 공통 클릭 리스너 설정
        buttonList.forEach { (button, _) ->
            button?.setOnClickListener { onButtonClicked(button) } // 클릭 시 공통 처리 함수 호출

        }


        return binding?.root // 생성된 뷰 반환
    }
    // Firestore에서 가져온 버튼 상태를 기반으로 UI 업데이트
    private fun updateButtonStates(buttonStates: Map<String, Boolean>) {
        // 모든 버튼을 GONE 상태로 초기화
        binding?.selectedButtonTravel?.visibility = View.GONE
        binding?.selectedButtonScience?.visibility = View.GONE
        binding?.selectedButtonFinance?.visibility = View.GONE
        binding?.selectedButtonAnimal?.visibility = View.GONE
        binding?.selectedButtonMusic?.visibility = View.GONE
        binding?.selectedButtonFood?.visibility = View.GONE
        binding?.selectedButtonBeauty?.visibility = View.GONE
        binding?.selectedButtonBook?.visibility = View.GONE
        binding?.selectedButtonCloth?.visibility = View.GONE

        // 버튼 상태에 따라 VISIBLE로 설정
        buttonStates.forEach { (key, value) ->
            if (value) { // Boolean 값이 true인 경우
                when (key) {
                    "Travel" -> binding?.selectedButtonTravel?.visibility = View.VISIBLE
                    "Science" -> binding?.selectedButtonScience?.visibility = View.VISIBLE
                    "Finance" -> binding?.selectedButtonFinance?.visibility = View.VISIBLE
                    "Animal" -> binding?.selectedButtonAnimal?.visibility = View.VISIBLE
                    "Music" -> binding?.selectedButtonMusic?.visibility = View.VISIBLE
                    "Food" -> binding?.selectedButtonFood?.visibility = View.VISIBLE
                    "Beauty" -> binding?.selectedButtonBeauty?.visibility = View.VISIBLE
                    "Book" -> binding?.selectedButtonBook?.visibility = View.VISIBLE
                    "Cloth" -> binding?.selectedButtonCloth?.visibility = View.VISIBLE
                }
            }
        }
    }


    // 버튼 클릭 시 호출되는 공통 처리 함수
    private fun onButtonClicked(selectedButton: ImageButton) {
        // 선택된 버튼에 따른 Fragment 결정
        val fragment = when (selectedButton) {
            binding?.selectedButtonTravel -> DefaultFragment()
            binding?.selectedButtonScience -> ScienceFragment()
            binding?.selectedButtonFinance -> FinanceFragment()
            binding?.selectedButtonAnimal -> DefaultFragment()
            binding?.selectedButtonMusic -> DefaultFragment()
            binding?.selectedButtonFood -> FoodFragment()
            binding?.selectedButtonBeauty -> DefaultFragment()
            binding?.selectedButtonBook -> DefaultFragment()
            binding?.selectedButtonCloth -> DefaultFragment()
            else -> null
        }

        // 모든 버튼의 이미지를 기본 상태로 리셋
        buttonList.forEach { (button, defaultDrawable) ->
            button?.setImageResource(defaultDrawable) // 기본 이미지로 설정
        }

        // 선택된 버튼만 선택 상태 이미지로 설정
        selectedButton.setImageResource(
            when (selectedButton) {
                binding?.selectedButtonTravel -> R.drawable.travel_button_selected
                binding?.selectedButtonScience -> R.drawable.science_button_selected
                binding?.selectedButtonFinance -> R.drawable.finance_button_selected
                binding?.selectedButtonAnimal -> R.drawable.animal_button_selected
                binding?.selectedButtonMusic -> R.drawable.music_button_selected
                binding?.selectedButtonFood -> R.drawable.food_button_selected
                binding?.selectedButtonBeauty -> R.drawable.beauty_button_selected
                binding?.selectedButtonBook -> R.drawable.book_button_selected
                binding?.selectedButtonCloth -> R.drawable.cloth_button_selected
                else -> R.drawable.default_button_image
            }
        )

        // 선택된 Fragment로 이동
        fragment?.let { navigateToFragment(it) }
    }
    // Fragment 전환을 처리하는 함수
    private fun navigateToFragment(fragment: Fragment) {  // 자식 프래그먼트 관리자를 통해 트랜잭션 시작
        childFragmentManager.beginTransaction() // 자식 프래그먼트 관리자를 사용하여 교체
            .replace(R.id.fragmentContainer, fragment)  // Fragment 교체
            .addToBackStack(null)  // 백스택에 추가하여 뒤로 가기 가능
            .commit() // 트랜잭션 적용
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ViewBinding 해제하여 메모리 누수 방지
    }
}
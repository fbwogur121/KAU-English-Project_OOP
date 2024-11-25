package com.example.kau_english_oop.Camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.kau_english_oop.MenuFragment
import com.example.kau_english_oop.R
import com.example.kau_english_oop.WriteFragment
import com.example.kau_english_oop.databinding.FragmentCameraBinding



class CameraFragment : Fragment() {

    private var binding: FragmentCameraBinding? = null

    // Fragment가 UI를 생성할 때 호출되는 메서드
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View Binding 초기화
        // Fragment와 연결된 레이아웃(XML)을 inflate
        binding = FragmentCameraBinding.inflate(inflater, container, false)

        // btnMenu 클릭 리스너 추가 - 재혁
        binding?.btnMenu?.setOnClickListener {
            // MenuFragment로 전환
            val menuFragment = MenuFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, menuFragment)
                .addToBackStack(null)
                .commit()
        }

        // MyDiary 버튼 클릭 리스너 추가
        binding?.btnMyDiary?.setOnClickListener {
            // MyDiary 버튼을 선택 상태로 설정
            setSelectedButton(binding?.btnMyDiary, R.drawable.diary_selected)
            // 다른 버튼을 초기 상태로 재설정
            resetOtherButtons(binding?.btnWrite to R.drawable.write_default, binding?.btnSearch to R.drawable.search_default)
            // MyDiaryFragment로 전환
            navigateToFragment(MyDiaryFragment())
        }
        // Write 버튼 클릭 리스너 추가
        binding?.btnWrite?.setOnClickListener {
            // Write 버튼을 선택 상태로 설정
            setSelectedButton(binding?.btnWrite, R.drawable.write_selected)
            // 다른 버튼을 초기 상태로 재설정
            resetOtherButtons(binding?.btnMyDiary to R.drawable.diary_default, binding?.btnSearch to R.drawable.search_default)
            // WriteFragment로 전환
            navigateToFragment(WriteFragment())
        }
        // Search 버튼 클릭 리스너 추가
        binding?.btnSearch?.setOnClickListener {
            // Search 버튼을 선택 상태로 설정
            setSelectedButton(binding?.btnSearch, R.drawable.search_selected)
            // 다른 버튼을 초기 상태로 재설정
            resetOtherButtons(binding?.btnMyDiary to R.drawable.diary_default, binding?.btnWrite to R.drawable.write_default)
            // SearchFragment로 전환
            navigateToFragment(SearchFragment())
        }

        // Fragment의 root 뷰를 반환하여 화면에 표시
        return binding?.root
    }
    // 특정 버튼을 선택 상태로 설정하는 함수
    private fun setSelectedButton(button: ImageButton?, selectedDrawable: Int) {
        // 버튼의 이미지를 선택된 상태의 Drawable로 변경
        button?.setImageResource(selectedDrawable)
    }
    // 다른 버튼을 초기 상태로 재설정하는 함수
    private fun resetOtherButtons(vararg buttonDrawablePairs: Pair<ImageButton?, Int>) {
        // 전달받은 버튼-이미지 쌍 리스트를 반복 처리
        buttonDrawablePairs.forEach { (button, drawable) ->
            button?.setImageResource(drawable) // 버튼 이미지를 초기 상태로 변경
        }
    }
    // Fragment 전환을 처리하는 함수
    private fun navigateToFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction() // 자식 프래그먼트 관리자를 사용하여 교체
            .replace(R.id.fragmentContainer, fragment) // fragmentContainer를 새로운 Fragment로 교체
            .addToBackStack(null)
            .commit()  // 트랜잭션 적용
    }

    // Fragment의 뷰가 제거될 때 호출되는 메서드
    // View Binding 객체를 null로 설정하여 메모리 누수 방지
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}




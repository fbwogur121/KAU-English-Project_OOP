package com.example.kau_english_oop.Camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.DetailViewFragment
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentMyDiaryBinding


class MyDiaryFragment : Fragment() {

    private var binding: FragmentMyDiaryBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding 초기화
        binding=FragmentMyDiaryBinding.inflate(inflater,container,false)

        // "최근 보기" 버튼 클릭 리스너
        binding?.recentlyButton?.setOnClickListener {
            // DetailViewFragment로 이동
            navigateToFragment(DetailViewFragment())
        }
        // "올해 보기" 버튼 클릭 리스너
        binding?.thisYearButton?.setOnClickListener {
            // ThisYearFragment로 이동
            navigateToFragment(ThisYearFragment())
        }
        // "시간별 정렬" 버튼 클릭 리스너
        binding?.timeArrangeButton?.setOnClickListener {
            // TimeArrangeFragment로 이동
            navigateToFragment(TimeArrangeFragment())
        }
        // "내가 좋아요한 다이어리" 버튼 클릭 리스너
        binding?.myFavoriteButton?.setOnClickListener {
            // FavoriteDiaryFragment로 이동
            navigateToFragment(FavoriteDiaryFragment())
        }
        // 생성된 뷰 반환
        return binding?.root
    }
    // Fragment 전환 함수
    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction() // 부모 FragmentManager를 통해 트랜잭션 시작
            .replace(R.id.fragmentContainer, fragment)  // 현재 Fragment를 새로운 Fragment로 교체
            .addToBackStack(null)  // 백스택에 추가하여 뒤로 가기 기능 활성화
            .commit()  // 트랜잭션 적용
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ViewBinding 해제하여 메모리 누수 방지
    }
}

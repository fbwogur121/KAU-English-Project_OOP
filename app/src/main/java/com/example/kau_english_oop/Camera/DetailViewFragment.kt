package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kau_english_oop.databinding.FragmentDetailViewBinding

// 이미지 데이터를 표시하는 Fragment
class DetailViewFragment : Fragment() {

    private var binding: FragmentDetailViewBinding? = null // ViewBinding 객체
    private lateinit var viewModel: DetailViewViewModel // ViewModel 객체
    private lateinit var adapter: DetailViewAdapter  // RecyclerView 어댑터

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailViewBinding.inflate(inflater, container, false)


        // ViewModel 초기화
        viewModel = ViewModelProvider(requireActivity())[DetailViewViewModel::class.java]

        // RecyclerView 초기화 및 Adapter 설정
        adapter = DetailViewAdapter(emptyList(), viewModel)  // 어댑터에 빈 리스트 전달
        binding?.detailviewRecyclerveiw?.layoutManager = LinearLayoutManager(requireContext())  // 레이아웃 매니저 설정
        binding?.detailviewRecyclerveiw?.adapter = adapter // 어댑터 연결


        // ViewModel의 LiveData 관찰
        viewModel.images.observe(viewLifecycleOwner) { images ->
            adapter.updateData(images) // RecyclerView 데이터 업데이트
        }

        // Firestore 데이터 가져오기
        viewModel.fetchRecentImages()



        return binding?.root  // 생성된 뷰 반환
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ViewBinding 정리
    }
}

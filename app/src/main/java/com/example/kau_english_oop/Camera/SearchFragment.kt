package com.example.kau_english_oop.Camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kau_english_oop.DetailViewAdapter
import com.example.kau_english_oop.DetailViewViewModel
import com.example.kau_english_oop.databinding.FragmentDetailViewBinding
import com.example.kau_english_oop.databinding.FragmentSearchBinding

// 검색 기능을 제공하는 Fragment
class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding?=null
    private lateinit var viewModel: DetailViewViewModel
    private lateinit var adapter: DetailViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        // ViewModel 초기화
        viewModel = ViewModelProvider(requireActivity())[DetailViewViewModel::class.java]
        // RecyclerView 초기화
        adapter = DetailViewAdapter(emptyList(), viewModel)
        binding.searchRecyclerveiw.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerveiw.adapter = adapter

        // 검색 결과 데이터를 관찰
        viewModel.searchResults.observe(viewLifecycleOwner) { images ->
            adapter.updateData(images)  // 데이터 변경 시 RecyclerView 업데이트
        }


        // 검색 버튼 클릭 이벤트
        binding?.searchIconButton?.setOnClickListener {

            val search_input = binding?.searchEditText?.text.toString() // 입력된 설명 가져오기
            // 한국어가 포함되었는지 검사하는 함수
            if (containsKorean(search_input)) {
                Toast.makeText(requireContext(), "검색은 한국어를 포함할 수 없습니다. 다시 입력해 주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener // 함수 종료하여 업로드 중단
            }

            if (search_input.isEmpty()) {
                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show() // 유효성 검사
                return@setOnClickListener
            }
            viewModel.fetchSearchResults(search_input) // 검색 실행
        }


        return binding?.root  // 뷰 반환
    }


    // 한국어 포함 여부를 검사하는 함수
    private fun containsKorean(text: String): Boolean {
        return text.any { it in '\uAC00'..'\uD7A3' } // 한국어 유니코드 범위에 해당하는 문자가 있는지 검사
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ViewBinding 정리
    }
}
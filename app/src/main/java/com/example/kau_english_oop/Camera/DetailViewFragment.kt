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

class DetailViewFragment : Fragment() {

    private var binding: FragmentDetailViewBinding? = null
    private lateinit var viewModel: DetailViewViewModel
    private lateinit var adapter: DetailViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailViewBinding.inflate(inflater, container, false)


        // ViewModel 초기화
        viewModel = ViewModelProvider(requireActivity())[DetailViewViewModel::class.java]

        // RecyclerView 초기화 및 Adapter 설정
        adapter = DetailViewAdapter(emptyList(), viewModel)
        binding?.detailviewRecyclerveiw?.layoutManager = LinearLayoutManager(requireContext())
        binding?.detailviewRecyclerveiw?.adapter = adapter

        // 최근의 데이터를 관찰
        viewModel.images.observe(viewLifecycleOwner) { images ->
            adapter.updateData(images)
        }

        // 데이터를 가져옴
        viewModel.fetchRecentImages()



        return binding?.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ViewBinding 정리
    }
}

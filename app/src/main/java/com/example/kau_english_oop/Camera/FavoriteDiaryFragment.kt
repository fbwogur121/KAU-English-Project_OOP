package com.example.kau_english_oop.Camera

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kau_english_oop.DetailViewAdapter
import com.example.kau_english_oop.DetailViewViewModel
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentFavoriteDiaryBinding
import com.example.kau_english_oop.databinding.FragmentThisYearBinding


class FavoriteDiaryFragment : Fragment() {
    private var binding: FragmentFavoriteDiaryBinding? = null
    private lateinit var viewModel: DetailViewViewModel
    private lateinit var adapter: DetailViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFavoriteDiaryBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(requireActivity())[DetailViewViewModel::class.java]
        adapter = DetailViewAdapter(emptyList(), viewModel)

        binding?.favoriteDiaryRecyclerveiw?.layoutManager = LinearLayoutManager(requireContext())
        binding?.favoriteDiaryRecyclerveiw?.adapter = adapter

        // like가 true인 데이터만 추출
        viewModel.favoriteDiaryImages.observe(viewLifecycleOwner) { images ->
            adapter.updateData(images)
        }

        // 데이터를 가져옴
        viewModel.fetchFavoriteDiary()



        return binding?.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ViewBinding 정리
    }
}
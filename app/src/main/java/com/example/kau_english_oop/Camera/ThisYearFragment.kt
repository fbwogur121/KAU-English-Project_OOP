package com.example.kau_english_oop.Camera

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kau_english_oop.DetailViewAdapter
import com.example.kau_english_oop.DetailViewViewModel
import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentDetailViewBinding
import com.example.kau_english_oop.databinding.FragmentThisYearBinding
import com.example.kau_english_oop.databinding.ItemDetailBinding
import com.google.firebase.firestore.FirebaseFirestore


class ThisYearFragment : Fragment() {
    private var binding: FragmentThisYearBinding? = null
    private lateinit var viewModel: DetailViewViewModel
    private lateinit var adapter: DetailViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentThisYearBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(requireActivity())[DetailViewViewModel::class.java]
        adapter = DetailViewAdapter(emptyList(), viewModel)

        binding?.thisYearviewRecyclerveiw?.layoutManager = LinearLayoutManager(requireContext())
        binding?.thisYearviewRecyclerveiw?.adapter = adapter

        // 올해의 데이터를 관찰
        viewModel.thisYearImages.observe(viewLifecycleOwner) { images ->
            adapter.updateData(images)
        }

        // 데이터를 가져옴
        viewModel.fetchThisYearImages()



        return binding?.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ViewBinding 정리
    }

}


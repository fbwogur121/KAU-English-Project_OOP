package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kau_english_oop.databinding.FragmentLikeResultBinding


class LikeResultFragment : Fragment() {
    private var binding: FragmentLikeResultBinding? = null
    private val viewModel: LikeResultViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikeResultBinding.inflate(inflater, container, false)
        val view = binding?.root

        binding?.recyclerViewResults?.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ResultAdapter()
        binding?.recyclerViewResults?.adapter = adapter

        viewModel.selectedDrawables.observe(viewLifecycleOwner) { drawables ->
            viewModel.fetchApiData(drawables) { data ->
                adapter.submitList(data)
            }
        }

        binding?.buttonNavigate?.setOnClickListener {
            // Implement navigation to the related site
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}


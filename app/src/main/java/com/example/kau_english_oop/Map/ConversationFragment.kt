package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kau_english_oop.Adapter.ConversationAdapter
import com.example.kau_english_oop.databinding.FragmentConversationBinding
import com.example.kau_english_oop.ViewModel.ConversationViewModel

class ConversationFragment : Fragment() {

    private var _binding: FragmentConversationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ConversationViewModel
    private lateinit var adapter: ConversationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentConversationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConversationViewModel::class.java)

        val buildingName = arguments?.getString("buildingName")
        binding.titleTextView.text = buildingName ?: "No Building Name"

        setupRecyclerView()
        buildingName?.let { viewModel.fetchConversation(it) }

        // btnMenu 클릭 리스너 추가
        binding?.btnMenu?.setOnClickListener {
            // MenuFragment로 전환
            val menuFragment = MenuFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, menuFragment)
                .addToBackStack(null)
                .commit()
        }

        viewModel.conversationList.observe(viewLifecycleOwner, Observer { conversations ->
            adapter.updateConversations(conversations)
        })

        binding.exitButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        adapter = ConversationAdapter(mutableListOf())
        binding.conversationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.conversationRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
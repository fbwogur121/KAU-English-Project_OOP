package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.databinding.FragmentConversationBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ConversationFragment : Fragment() {

    private var _binding: FragmentConversationBinding? = null
    private val binding get() = _binding!!
    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConversationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 빌딩 이름을 TextView에 설정
        val buildingName = arguments?.getString("buildingName")
        binding.titleTextView.text = buildingName ?: "No Building Name"

        // Firestore에서 대화 내용 가져오기
        buildingName?.let { fetchConversation(it) }

        binding.exitButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun fetchConversation(buildingName: String) {
        firestore.collection("conversations")
            .whereEqualTo("buildingName", buildingName.trim()) // 공백 제거
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    binding.conversationTextView.text = "No conversation for ${buildingName} is found."
                } else {
                    for (document in documents) {
                        val conversation = document.getString("conversation")
                        binding.conversationTextView.text = conversation ?: "No conversation available."
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error getting conversation: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
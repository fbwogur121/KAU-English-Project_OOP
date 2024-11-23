package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kau_english_oop.Adapter.ConversationAdapter
import com.example.kau_english_oop.databinding.FragmentConversationBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//class ConversationFragment : Fragment() {
//
//    private var _binding: FragmentConversationBinding? = null
//    private val binding get() = _binding!!
//    private val firestore: FirebaseFirestore = Firebase.firestore
//
//    // Recycler View
//    private lateinit var adapter: ConversationAdapter
//    private val conversationList: mutableListOf<String>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentConversationBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // 전달받은 빌딩 이름을 TextView에 설정
//        val buildingName = arguments?.getString("buildingName")
//        binding.titleTextView.text = buildingName ?: "No Building Name"
//
//        // Firestore에서 대화 내용 가져오기
//        buildingName?.let { fetchConversation(it) }
//
//        binding.exitButton.setOnClickListener {
//            parentFragmentManager.popBackStack()
//        }
//    }
//
//    private fun fetchConversation(buildingName: String) {
//        firestore.collection("conversations")
//            .whereEqualTo("buildingName", buildingName.trim()) // 공백 제거
//            .get()
//            .addOnSuccessListener { documents ->
//                if (documents.isEmpty) {
//                    binding.conversationTextView.text = "No conversation for ${buildingName} is found."
//                } else {
//                    for (document in documents) {
//                        val conversation = document.getString("conversation")
//                        //binding.conversationTextView.text = conversation ?: "No conversation available."
//                        if (conversation != null) {
//                            // 숫자와 마침표를 기준으로 줄바꿈 추가
//                            val formattedConversation = conversation.replace(" \\d+\\. ".toRegex(), "\n")
//                            binding.conversationTextView.text = formattedConversation
//                        } else {
//                            binding.conversationTextView.text = "No conversation available."
//                        }
//                    }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(requireContext(), "Error getting conversation: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
class ConversationFragment : Fragment() {

    private var _binding: FragmentConversationBinding? = null
    private val binding get() = _binding!!
    private val firestore: FirebaseFirestore = Firebase.firestore
    private lateinit var adapter: ConversationAdapter
    private val conversationList = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentConversationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buildingName = arguments?.getString("buildingName")
        binding.titleTextView.text = buildingName ?: "No Building Name"

        setupRecyclerView()
        buildingName?.let { fetchConversation(it) }

        binding.exitButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        adapter = ConversationAdapter(conversationList)
        binding.conversationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.conversationRecyclerView.adapter = adapter
    }

    private fun fetchConversation(buildingName: String) {
        firestore.collection("conversations")
            .whereEqualTo("buildingName", buildingName.trim())
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    conversationList.add("No conversation for ${buildingName} is found.")
                } else {
                    for (document in documents) {
                        val conversation = document.getString("conversation")
                        if (conversation != null) {
                            // // 숫자와 마침표를 기준으로 줄바꿈 추가
                            val formattedConversation = conversation.replace(Regex("\\d+\\."), "\n").trim()
                            conversationList.add(formattedConversation)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
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
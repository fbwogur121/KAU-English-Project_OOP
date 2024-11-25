package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.databinding.FragmentMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class MenuFragment : Fragment() {

    private var binding: FragmentMenuBinding? = null
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserInfo()

        binding?.etInstagram?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                saveInstagramInfo()
            }
        }

        binding?.etGithub?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                saveGithubInfo()
            }
        }

        // btnBack 클릭 리스너 추가
        binding?.btnBack?.setOnClickListener {
            // 이전 화면으로 돌아가기
            requireActivity().onBackPressed()
        }
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        user?.let {
            binding?.etID?.setText(it.displayName) // displayName을 Name 칸에 설정
            binding?.etEmail?.setText(it.email) // email을 Email 칸에 설정

            // Firestore에서 Instagram 및 GitHub 정보 가져오기
            firestore.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        binding?.etInstagram?.setText(document.getString("instagram") ?: "")
                        binding?.etGithub?.setText(document.getString("github") ?: "")
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error getting user info: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveInstagramInfo() {
        val user = auth.currentUser
        val instagram = binding?.etInstagram?.text.toString()

        if (user != null && instagram.isNotEmpty()) {
            firestore.collection("users").document(user.uid)
                .set(mapOf("instagram" to instagram), SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Instagram info saved.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error saving Instagram info: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun saveGithubInfo() {
        val user = auth.currentUser
        val github = binding?.etGithub?.text.toString()

        if (user != null && github.isNotEmpty()) {
            firestore.collection("users").document(user.uid)
                .set(mapOf("github" to github), SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "GitHub info saved.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error saving GitHub info: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
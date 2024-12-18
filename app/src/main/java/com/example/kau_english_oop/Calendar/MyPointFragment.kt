package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.databinding.FragmentMyPointBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPointFragment : Fragment() {

    private lateinit var binding: FragmentMyPointBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPointBinding.inflate(inflater, container, false)

        // 포인트 불러오기
        fetchPoints()

        return binding.root
    }

    private fun fetchPoints() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userDocRef = firestore.collection("users").document(userId)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.contains("point")) {
                        val points = document.getLong("point") ?: 0
                        binding.tvPoints.text = "$points points"
                    } else {
                        binding.tvPoints.text = "0 points"
                    }
                }
                .addOnFailureListener {
                    binding.tvPoints.text = "Failed to load points"
                }
        }
    }
}

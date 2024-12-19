package com.example.kau_english_oop.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ConversationViewModel : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // 대화 데이터를 가져오고, 이를 LiveData로 변환하여 ConversationFragment에서 관찰할 수 있도록. -> viewModel 역할
    private val _conversationList = MutableLiveData<List<Pair<String, String>>>()
    val conversationList: LiveData<List<Pair<String, String>>> get() = _conversationList

    // firestore에서 건물정보 가져오기 -> model 역할.
    fun fetchConversation(buildingName: String) {
        firestore.collection("conversations")
            .whereEqualTo("buildingName", buildingName.trim())
            .get()
            .addOnSuccessListener { documents ->
                val conversations = mutableListOf<Pair<String, String>>()
                if (documents.isEmpty) {
                    conversations.add(Pair("No conversation for $buildingName is found.", ""))
                } else {
                    for (document in documents) {
                        val conversation = document.getString("conversation")
                        val imageUrl = document.getString("imageUrl") // 이미지 URL 가져오기
                        if (conversation != null) {
                            val formattedConversation = conversation.replace(Regex("\\d+\\."), "\n").trim()
                            conversations.add(Pair(formattedConversation, imageUrl ?: ""))
                        }
                    }
                }
                _conversationList.postValue(conversations)
            }
            .addOnFailureListener { exception ->
                Log.e("ConversationViewModel", "Error getting conversation: ${exception.message}")
            }
    }
}
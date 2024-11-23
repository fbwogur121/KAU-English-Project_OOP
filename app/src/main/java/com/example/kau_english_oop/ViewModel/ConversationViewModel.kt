package com.example.kau_english_oop.ViewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ConversationViewModel : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _conversationList = MutableLiveData<List<String>>()
    val conversationList: LiveData<List<String>> get() = _conversationList

    fun fetchConversation(buildingName: String) {
        firestore.collection("conversations")
            .whereEqualTo("buildingName", buildingName.trim())
            .get()
            .addOnSuccessListener { documents ->
                val conversations = mutableListOf<String>()
                if (documents.isEmpty) {
                    conversations.add("No conversation for $buildingName is found.")
                } else {
                    for (document in documents) {
                        val conversation = document.getString("conversation")
                        if (conversation != null) {
                            val formattedConversation = conversation.replace(Regex("\\d+\\."), "\n").trim()
                            conversations.add(formattedConversation)
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
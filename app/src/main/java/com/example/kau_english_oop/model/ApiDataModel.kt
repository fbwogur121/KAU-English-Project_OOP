package com.example.kau_english_oop.model

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class ApiDataModel {
    private val db = FirebaseFirestore.getInstance()

    fun fetchDataByCategory(category: String, callback: (List<Map<String, Any>>) -> Unit) {
        db.collection("api_data")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { documents ->
                val dataList = mutableListOf<Map<String, Any>>()
                for (document in documents) {
                    dataList.add(document.data)
                }
                callback(dataList)
            }
            .addOnFailureListener { exception ->
                Log.w("FirebaseError", "Error getting documents: ", exception)
                callback(emptyList())
            }
    }
}

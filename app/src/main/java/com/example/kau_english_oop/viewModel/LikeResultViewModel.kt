package com.example.kau_english_oop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class LikeResultViewModel : ViewModel() {
    private val _selectedDrawables = MutableLiveData<List<Int>>()
    val selectedDrawables: LiveData<List<Int>> get() = _selectedDrawables

    private val db = FirebaseFirestore.getInstance()

    fun setSelectedDrawables(drawables: List<Int>) {
        _selectedDrawables.value = drawables
    }

    fun fetchApiData(drawables: List<Int>, callback: (List<Map<String, Any>>) -> Unit) {
        val results = mutableListOf<Map<String, Any>>()
        drawables.forEach { drawableId ->
            db.collection("api_data")
                .whereEqualTo("drawableId", drawableId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        results.add(document.data)
                    }
                    callback(results)
                }
                .addOnFailureListener { exception ->
                    Log.w("FirebaseError", "Error getting documents: ", exception)
                }
        }
    }
}

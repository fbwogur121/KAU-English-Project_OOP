package com.example.kau_english_oop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kau_english_oop.R

class HomeViewModel : ViewModel() {
    private val _imageButtonList = MutableLiveData<List<Int>>()
    val imageButtonList: LiveData<List<Int>> get() = _imageButtonList

    init {
        loadImageButtonList()
    }

    private fun loadImageButtonList() {
        _imageButtonList.value = listOf(
            R.drawable.lexilearn,
            R.drawable.journal_button,
            R.drawable.youtube_button,
            R.drawable.today_button,
            R.drawable.shopping_button,
            R.drawable.youtube_button,
            R.drawable.today_button,
            R.drawable.shopping_button
        )
    }
}
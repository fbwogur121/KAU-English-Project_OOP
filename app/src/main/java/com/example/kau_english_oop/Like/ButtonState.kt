package com.example.kau_english_oop.Like

import android.widget.ImageButton

data class ButtonState(
    val button: ImageButton?,
    val selectedDrawable: Int,
    val unselectedDrawable: Int,
    var isSelected: Boolean = false
)

package com.example.kau_english_oop.Like

import android.widget.ImageButton

// 버튼 상태를 나타내는 데이터 클래스
data class ButtonState(
    val button: ImageButton?, // 관련된 버튼 객체
    val selectedDrawable: Int, // 선택 상태 이미지 리소스 ID
    val unselectedDrawable: Int, // 선택되지 않은 상태 이미지 리소스 ID
    var isSelected: Boolean = false // 현재 선택 상태 (기본값: false)
)
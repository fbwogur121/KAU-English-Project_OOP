package com.example.kau_english_oop.model

// dataBase를 관리하는 data model
data class ContentModel(
    var explain: String? = null, // 사진 설명
    var imageUrl: String? = null, // 사진 주소
    var uid: String? = null, // 사용자 UID
    var userId: String? = null, // 사용자 이메일
    var timestamp: Long? = null, // 업로드 시간
    var favoriteCount: Int = 0, // 좋아요 카운트
    var favorites: MutableMap<String, Boolean> = HashMap() // 좋아요 정보
)


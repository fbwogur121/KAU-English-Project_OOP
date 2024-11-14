package com.example.kau_english_oop.model.GoogleAPI

data class PlaceResult(
    val name: String,
    val types: List<String>,
    val vicinity: String // 추가 정보로서 주위 주소
)
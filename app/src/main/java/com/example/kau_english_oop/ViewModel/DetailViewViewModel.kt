package com.example.kau_english_oop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DetailViewViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // 이미지 데이터를 관리하는 LiveData
    private val _images = MutableLiveData<List<Map<String, Any>>>()
    val images: LiveData<List<Map<String, Any>>> get() = _images

    // ThisYearFragment용 데이터
    private val _thisYearImages = MutableLiveData<List<Map<String, Any>>>()
    val thisYearImages: LiveData<List<Map<String, Any>>> get() = _thisYearImages

    // SearchFragment용 데이터
    private val _searchResults = MutableLiveData<List<Map<String, Any>>>()
    val searchResults: LiveData<List<Map<String, Any>>> get() = _searchResults

    // FavoriteDiaryFragment용 데이터
    private val _favoriteDiaryImages=MutableLiveData<List<Map<String, Any>>>()
    val favoriteDiaryImages:LiveData<List<Map<String, Any>>> get() = _favoriteDiaryImages

    // 공통 에러 메시지
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage




    fun fetchRecentImages() {
        // Firestore에서 images 컬렉션 데이터 가져오기
        db.collection("images")

            .get()
            .addOnSuccessListener { result ->
                val imageList = result.map { it.data } // 데이터를 리스트로 변환
                _images.value = imageList // LiveData에 데이터 저장
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "데이터를 불러오는 데 실패했습니다: ${exception.message}"
            }
    }

    // 올해의 데이터를 가져오는 메서드
    fun fetchThisYearImages() {
        val startOfYear = 1704067200000L // 2024년 1월 1일 00:00:00
        val endOfYear = 1735689599999L // 2024년 12월 31일 23:59:59

        db.collection("images")
            .whereGreaterThanOrEqualTo("timestamp", startOfYear)
            .whereLessThanOrEqualTo("timestamp", endOfYear)
            .get()
            .addOnSuccessListener { result ->
                val imageList = result.documents.map { it.data ?: emptyMap() }
                _thisYearImages.value = imageList
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "데이터를 불러오는 데 실패했습니다: ${exception.message}"
            }
    }

    // 검색 결과 데이터를 가져오는 메서드
    fun fetchSearchResults(query: String) {
        db.collection("images")
            .get()
            .addOnSuccessListener { result ->
                val filteredList = result.documents.filter { document ->
                    val explanation = document.getString("explanation") ?: ""
                    explanation.contains(query, ignoreCase = true)
                }.map { it.data ?: emptyMap() }
                _searchResults.value = filteredList
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "검색 실패: ${exception.message}"
            }
    }

    fun fetchFavoriteDiary() {
        db.collection("images")
            .whereEqualTo("like", true) // Firestore 쿼리에서 필터링
            .get()
            .addOnSuccessListener { result ->
                val filteredList = result.documents.map { it.data ?: emptyMap() }
                _favoriteDiaryImages.value = filteredList
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = "데이터를 불러오는 데 실패했습니다: ${exception.message}"
            }
    }



    // 좋아요 상태 업데이트
    fun updateLikeStatus(documentId: String, isLiked: Boolean) {
        db.collection("images").document(documentId)
            .update("like", isLiked)
            .addOnSuccessListener {
                println("Firestore 좋아요 상태 업데이트 성공")
            }
            .addOnFailureListener { exception ->
                println("Firestore 좋아요 상태 업데이트 실패: ${exception.message}")
            }
    }


}

package com.example.kau_english_oop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// Firestore 데이터를 관리하는 ViewModel
class DetailViewViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance() // Firestore 인스턴스

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



    // Firestore에서 최신 이미지 데이터를 가져오는 메서드
    fun fetchRecentImages() {
        // Firestore에서 images 컬렉션 데이터 가져오기
        db.collection("images")
            .get()  // 컬렉션 내 모든 문서를 가져옴
            .addOnSuccessListener { result ->
                // 가져온 결과를 데이터 리스트로 변환
                val imageList = result.map { it.data }  // 각 문서 데이터를 Map으로 변환
                _images.value = imageList  // 변환된 데이터를 LiveData에 저장
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기에 실패했을 경우 에러 메시지 업데이트
                _errorMessage.value = "데이터를 불러오는 데 실패했습니다: ${exception.message}"
            }
    }

    // Firestore에서 올해 데이터를 가져오기
    fun fetchThisYearImages() {
        // 2024년의 시작과 끝을 밀리초 단위로 계산
        val startOfYear = 1704067200000L // 2024년 1월 1일 00:00:00
        val endOfYear = 1735689599999L // 2024년 12월 31일 23:59:59

        db.collection("images") // Firestore의 "images" 컬렉션을 참조
            .whereGreaterThanOrEqualTo("timestamp", startOfYear) // timestamp가 2024년 1월 1일 이후
            .whereLessThanOrEqualTo("timestamp", endOfYear) // timestamp가 2024년 12월 31일 이전
            .get()  // 조건에 맞는 문서를 가져옴
            .addOnSuccessListener { result ->
                // 가져온 결과를 데이터 리스트로 변환
                val imageList = result.documents.map { it.data ?: emptyMap() }  // 각 문서 데이터를 Map으로 변환
                _thisYearImages.value = imageList // 변환된 데이터를 LiveData에 저장
            }
            .addOnFailureListener { exception ->
                // 데이터 가져오기에 실패했을 경우 에러 메시지 업데이트
                _errorMessage.value = "데이터를 불러오는 데 실패했습니다: ${exception.message}"
            }
    }

    // Firestore에서 검색어를 포함한 데이터 가져오기
    fun fetchSearchResults(query: String) {
        db.collection("images") // Firestore의 "images" 컬렉션을 참조
            .get() // 모든 문서를 가져옴
            .addOnSuccessListener { result ->
                // 가져온 결과에서 조건에 맞는 데이터 필터링
                val filteredList = result.documents.filter { document ->
                    val explanation = document.getString("explanation") ?: "" // "explanation" 필드 값 가져오기
                    explanation.contains(query, ignoreCase = true) // 검색어를 포함하는지 확인 (대소문자 무시)
                }.map { it.data ?: emptyMap() } // 필터링된 데이터를 Map으로 변환, null 방지
                _searchResults.value = filteredList  // 필터링된 결과를 LiveData에 저장
            }
            .addOnFailureListener { exception ->
                // 실패 시 에러 메시지를 LiveData에 저장
                _errorMessage.value = "검색 실패: ${exception.message}"
            }
    }

    // 좋아요 상태가 true인 데이터를 가져오기
    fun fetchFavoriteDiary() {
        db.collection("images") // Firestore의 "images" 컬렉션을 참조
            // "like" 필드가 true인 문서만 가져오기
            .whereEqualTo("like", true) // Firestore 쿼리에서 필터링
            .get()
            .addOnSuccessListener { result ->
                // 가져온 결과를 리스트로 변환
                val filteredList = result.documents.map { it.data ?: emptyMap() } // null 방지
                _favoriteDiaryImages.value = filteredList  // 결과를 LiveData에 저장
            }
            .addOnFailureListener { exception ->
                // 실패 시 에러 메시지를 LiveData에 저장
                _errorMessage.value = "데이터를 불러오는 데 실패했습니다: ${exception.message}"
            }
    }



    // 좋아요 상태 업데이트
    fun updateLikeStatus(documentId: String, isLiked: Boolean) {
        db.collection("images").document(documentId) // Firestore에서 특정 문서를 참조
            .update("like", isLiked)  // "like" 필드를 업데이트
            .addOnSuccessListener {
                // 성공 시 로그 출력
                println("Firestore 좋아요 상태 업데이트 성공")
            }
            .addOnFailureListener { exception ->
                // 실패 시 로그 출력
                println("Firestore 좋아요 상태 업데이트 실패: ${exception.message}")
            }
    }


}

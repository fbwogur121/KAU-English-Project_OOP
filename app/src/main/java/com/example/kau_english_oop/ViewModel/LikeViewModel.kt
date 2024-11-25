package com.example.kau_english_oop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kau_english_oop.Like.ButtonState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Firestore와 상호작용하며 버튼 상태를 관리하는 ViewModel
class LikeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance() // Firestore 인스턴스
    private val auth = FirebaseAuth.getInstance() // Firebase Auth 인스턴스

    // LiveData로 버튼 상태 관리
    private val _buttonStatesMap = MutableLiveData< Map<String, Boolean>>()
    val buttonStatesMap: LiveData< Map<String, Boolean>> get() = _buttonStatesMap

    // 업로드 성공 여부
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> get() = _uploadStatus

    // 에러 메시지
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Firestore에 버튼 상태 저장
    fun saveToFirestore(buttonStates: List<ButtonState>) {
        // 버튼 상태를 Firestore에 저장할 데이터로 변환
        val data = mapOf(
            "uid" to auth.currentUser?.uid, // 현재 사용자의 UID
            "email" to auth.currentUser?.email, // 현재 사용자의 이메일
            // Firestore에 저장할 "buttonStates" 필드의 데이터를 생성
            "buttonStates" to buttonStates.associate { buttonState ->
                // 버튼의 contentDescription(버튼의 고유 이름)을 키(key)로 사용
                // contentDescription이 null일 경우 기본값으로 빈 문자열("") 사용
                // 버튼의 선택 상태(isSelected)를 값(value)로 사용
                buttonState.button?.contentDescription.toString() to buttonState.isSelected
                // 결과적으로 Map<String, Boolean> 형태가 생성됨
            }
            /* 다음과 같은 JSON형태로 Firestore에 저장
            {
              "buttonStates": {
                "Travel": true,
                "Science": false,
                "Finance": true
             }
            }
             */
        )
        // "favorites" 컬렉션에 문서를 저장
        db.collection("favorites").document(auth.currentUser?.uid ?: "default_user")
            .set(data) // 데이터를 저장하거나 덮어씌움
            .addOnSuccessListener {
                _uploadStatus.value = true // 업로드 성공 여부 업데이트
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Firestore 저장 실패: ${e.message}"
            }
    }
    // Firestore에서 버튼 상태를 가져오는 메서드
    fun fetchButtonStatesFromFirestore() {
        db.collection("favorites").document(auth.currentUser?.uid ?: "default_user")
            .get() // 특정 문서를 가져옴
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Firestore에서 가져온 데이터가 Map<String, Boolean> 형태인지 확인 후 변환
                    val buttonStatesMap = document.get("buttonStates") as? Map<String, Boolean>
                    _buttonStatesMap.value = buttonStatesMap ?: emptyMap() // LiveData에 Map 저장
                } else {
                    _buttonStatesMap.value = emptyMap() // 문서가 없을 경우 빈 Map 반환
                }
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Firestore 데이터 가져오기 실패: ${e.message}" // 에러 메시지 설정
            }
    }



}
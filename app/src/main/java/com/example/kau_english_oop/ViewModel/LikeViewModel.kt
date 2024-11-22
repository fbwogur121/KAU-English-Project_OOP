package com.example.kau_english_oop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kau_english_oop.Like.ButtonState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LikeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // LiveData로 버튼 상태 관리
    private val _buttonStatesMap = MutableLiveData< Map<String, Boolean>>()
    val buttonStatesMap: LiveData< Map<String, Boolean>> get() = _buttonStatesMap

    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> get() = _uploadStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Firestore에 버튼 상태 저장
    fun saveToFirestore(buttonStates: List<ButtonState>) {
        val data = mapOf(
            "uid" to auth.currentUser?.uid,
            "email" to auth.currentUser?.email,
            "buttonStates" to buttonStates.associate { buttonState ->
                buttonState.button?.contentDescription.toString() to buttonState.isSelected
            }
        )

        db.collection("favorites").document(auth.currentUser?.uid ?: "default_user")
            .set(data)
            .addOnSuccessListener {
                _uploadStatus.value = true
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Firestore 저장 실패: ${e.message}"
            }
    }

    fun fetchButtonStatesFromFirestore() {
        db.collection("favorites").document(auth.currentUser?.uid ?: "default_user")
            .get()
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

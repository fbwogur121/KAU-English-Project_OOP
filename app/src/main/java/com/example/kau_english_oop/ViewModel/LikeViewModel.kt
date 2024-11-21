package com.example.kau_english_oop.ViewModel

import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kau_english_oop.R
import com.example.kau_english_oop.model.ButtonState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LikeViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // LiveData로 버튼 상태 관리
    private val _buttonStates = MutableLiveData<Map<String, Boolean>?>()
    val buttonStates: LiveData<Map<String, Boolean>?> get() = _buttonStates

    // 선택된 버튼 리스트를 LiveData로 관리
    private val _selectedButtons = MutableLiveData<List<String>>()
    val selectedButtons: LiveData<List<String>> get() = _selectedButtons



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
                buttonState.button.contentDescription.toString() to buttonState.isSelected
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
                    val buttonStatesMap = document.get("buttonStates") as? Map<String, Boolean>
                    val buttonStatesList = buttonStatesMap?.map { (key, value) ->
                        ButtonState(
                            button = ImageButton(null), // Fragment에서 연결
                            selectedDrawable =0,// Fragment에서 업데이트
                            unselectedDrawable =0, // Fragment에서 업데이트
                            isSelected = value
                        )
                    } ?: emptyList()

                    _buttonStates.value = buttonStatesMap
                }
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Firestore 데이터 가져오기 실패: ${e.message}"
            }
    }

    // 선택된 버튼 업데이트
    /*fun setSelectedButtons(buttons: List<String>) {
        _selectedButtons.value = buttons
    }
    */
}

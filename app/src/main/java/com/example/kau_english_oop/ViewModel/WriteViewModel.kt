package com.example.kau_english_oop

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

// Firebase와 상호작용하는 로직을 처리하는 ViewModel
class WriteViewModel : ViewModel() {

    // Firebase 서비스 초기화
    private val storage = FirebaseStorage.getInstance() // Firebase Storage 인스턴스
    private val db = FirebaseFirestore.getInstance() // Firebase Firestore 인스턴스
    private val auth = FirebaseAuth.getInstance() // Firebase Auth 인스턴스

    // 업로드 성공 여부를 담는 LiveData
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean> get() = _uploadStatus

    // 에러 메시지를 담는 LiveData
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Firebase Storage에 이미지 업로드하고 Firestore에 저장
    fun uploadImage(imageUri: Uri, explanation: String) {
        val user = auth.currentUser // 현재 로그인한 사용자 가져오기
        if (user == null) {
            _errorMessage.value = "사용자가 로그인되어 있지 않습니다." // 로그인 여부 확인
            return
        }

        val uid = user.uid // Firebase UID
        val email = user.email ?: "unknown" // 사용자 이메일 (null 대비 기본값 설정)
        val documentId = UUID.randomUUID().toString() // 문서 고유 ID 생성

        // 현재 시간을 기준으로 파일 이름 생성
        var time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()) // 현재 시간을 "yyyyMMdd_HHmmss" 형식의 문자열로 변환
        var imageFileName = "IMAGE_$time.png" // 이미지 파일 이름 생성
        var storagePath = storage.reference.child("images").child(imageFileName) // Firebase Storage 경로 설정

        // Firebase Storage에 이미지 업로드
        storagePath.putFile(imageUri)
            .addOnSuccessListener {
                // 업로드 성공 시 다운로드 URL 가져오기
                storagePath.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveToFirestore(documentId,downloadUri.toString(), explanation, uid, email,false) // Firestore에 저장
                }.addOnFailureListener { e ->
                    _errorMessage.value = "URL 가져오기 실패: ${e.message}" // URL 가져오기 실패
                }
            }.addOnFailureListener { e ->
                _errorMessage.value = "이미지 업로드 실패: ${e.message}" // 업로드 실패
            }
    }

    // Firestore에 데이터 저장
    private fun saveToFirestore(documentId:String,imageUrl: String, explanation: String, uid: String, email: String,like:Boolean) {
        val data = mapOf(
            "documentId" to documentId,
            "imageUrl" to imageUrl, // 업로드된 이미지 URL
            "explanation" to explanation, // 이미지 설명
            "uid" to uid, // 사용자 UID
            "email" to email, // 사용자 이메일
            "timestamp" to System.currentTimeMillis(), // 업로드 시간
            "like" to false  // 초기 좋아요 상태

        )

        db.collection("images").document(documentId)
            .set(data, SetOptions.merge()) // 기존 문서 업데이트 또는 새로 생성
            .addOnSuccessListener {
                _uploadStatus.value = true // 업로드 성공 상태 설정
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Firestore 저장 실패: ${e.message}" // Firestore 저장 실패 메시지
            }

    }
}

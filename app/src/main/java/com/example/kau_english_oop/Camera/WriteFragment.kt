package com.example.kau_english_oop.Camera

// Import necessary libraries
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.kau_english_oop.databinding.FragmentWriteBinding
import com.example.kau_english_oop.model.ContentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class WriteFragment : Fragment() {
    private var binding: FragmentWriteBinding? = null

    // Firebase 초기화
    var auth = FirebaseAuth.getInstance() // Firebase 인증 객체 초기화
    var firestore = FirebaseFirestore.getInstance() // Firebase Firestore 객체 초기화
    var storage = FirebaseStorage.getInstance() // Firebase Storage 객체 초기화

    private var photoUri: Uri? = null // 선택한 사진의 URI를 저장할 변수

    // 사진 선택 결과를 처리하는 ActivityResultLauncher
    private val photoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // 결과가 성공적이면
            photoUri = result.data?.data // 선택한 사진의 URI를 photoUri 변수에 저장
            binding?.uploadImageview?.setImageURI(photoUri) // 선택한 사진을 ImageView에 표시
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteBinding.inflate(inflater, container, false) // FragmentWriteBinding을 통해 뷰를 생성하고 binding 변수에 할당

        // 사진 업로드 버튼 클릭 리스너 설정
        binding?.addphotoUploadBtn?.setOnClickListener {
            contentUpload() // contentUpload 함수를 호출하여 사진 업로드를 처리
        }

        // 사진 선택을 위한 Intent 생성
        val intent = Intent(Intent.ACTION_PICK) // 갤러리에서 사진을 선택하기 위한 Intent
        intent.type = "image/*" // 이미지 타입을 선택하도록 설정

        // 사진 선택 버튼 클릭 리스너 설정
        binding?.addPhotoBtn?.setOnClickListener {
            photoResult.launch(intent) // photoResult 런처를 사용해 Intent 실행 (갤러리에서 사진 선택)
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // binding을 null로 설정하여 메모리 누수 방지
    }

    // 사진을 Firebase Storage에 업로드하고 Firestore에 메타데이터 저장하는 함수
    private fun contentUpload() {
        // 설명 텍스트를 가져옴
        val explainText = binding?.addphotoEditEdittext?.text.toString()

        // 한국어가 포함되었는지 검사하는 함수
        if (containsKorean(explainText)) {
            Toast.makeText(requireContext(), "설명은 한국어를 포함할 수 없습니다. 다시 입력해 주세요.", Toast.LENGTH_LONG).show()
            return // 함수 종료하여 업로드 중단
        }

        // 현재 시간을 기준으로 파일 이름 생성
        var time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()) // 현재 시간을 "yyyyMMdd_HHmmss" 형식의 문자열로 변환
        var imageFileName = "IMAGE_$time.png" // 이미지 파일 이름 생성
        var storagePath = storage.reference.child("images").child(imageFileName) // Firebase Storage 경로 설정

        // Firebase Storage에 파일 업로드
        photoUri?.let { uri -> // photoUri가 null이 아닐 경우에만 실행
            storagePath.putFile(uri) // Firebase Storage에 파일 업로드
                .continueWithTask { storagePath.downloadUrl } // 업로드가 완료되면 다운로드 URL을 가져오는 작업 실행
                .addOnCompleteListener { task -> // 업로드 및 URL 가져오기 작업이 완료되었을 때 실행
                    if (task.isSuccessful) { // 작업이 성공했을 경우
                        val downloadUrl = task.result // 다운로드 URL을 가져옴
                        var contentModel = ContentModel().apply { // ContentModel 객체 생성 및 초기화
                            imageUrl = downloadUrl.toString() // 다운로드 URL을 imageUrl 속성에 저장
                            explain = binding?.addphotoEditEdittext?.text.toString() // 사용자가 입력한 설명을 explain 속성에 저장
                            uid = auth.uid // 현재 사용자의 UID를 uid 속성에 저장
                            userId = auth.currentUser?.email // 현재 사용자의 이메일을 userId 속성에 저장
                            timestamp = System.currentTimeMillis() // 현재 시간을 타임스탬프로 저장
                        }
                        // Firestore의 "images" 컬렉션에 데이터 저장
                        firestore.collection("images").document().set(contentModel)
                            .addOnSuccessListener {
                                // 데이터 저장 성공 시 사용자에게 업로드 성공 메시지 표시
                                Toast.makeText(requireContext(), "업로드 성공: $downloadUrl", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        // 작업 실패 시 사용자에게 업로드 실패 메시지 표시
                        Toast.makeText(requireContext(), "업로드 실패", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    // 한국어 포함 여부를 검사하는 함수
    private fun containsKorean(text: String): Boolean {
        return text.any { it in '\uAC00'..'\uD7A3' } // 한국어 유니코드 범위에 해당하는 문자가 있는지 검사
    }
}




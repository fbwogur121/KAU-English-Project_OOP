package com.example.kau_english_oop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kau_english_oop.databinding.FragmentWriteBinding

class WriteFragment : Fragment() {

    private var binding:FragmentWriteBinding?=null
    private lateinit var viewModel: WriteViewModel // ViewModel 초기화
    private var selectedImageUri: Uri? = null // 선택된 이미지의 Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteBinding.inflate(inflater, container, false) // ViewBinding 연결
        viewModel = ViewModelProvider(this)[WriteViewModel::class.java] // ViewModel 연결

        setupObservers() // LiveData 관찰 설정
        setupListeners() // UI 이벤트 처리

        return binding?.root
    }

    // UI 이벤트 처리
    private fun setupListeners() {
        // 이미지 선택 버튼 클릭 이벤트
        binding?.addPhotoBtn?.setOnClickListener {
            pickImage() // 이미지 선택
        }

        // 업로드 버튼 클릭 이벤트
        binding?.addphotoUploadBtn?.setOnClickListener {
            val explanation = binding?.addphotoEditEdittext?.text.toString() // 입력된 설명 가져오기
            // 한국어가 포함되었는지 검사하는 함수
            if (containsKorean(explanation)) {
                Toast.makeText(requireContext(), "설명은 한국어를 포함할 수 없습니다. 다시 입력해 주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener // 함수 종료하여 업로드 중단
            }

            if (selectedImageUri == null || explanation.isEmpty()) {
                Toast.makeText(requireContext(), "이미지와 설명을 입력하세요.", Toast.LENGTH_SHORT).show() // 유효성 검사
                return@setOnClickListener
            }
            viewModel.uploadImage(selectedImageUri!!, explanation) // ViewModel에 업로드 요청
        }
    }

    // LiveData 관찰 설정
    private fun setupObservers() {
        // 업로드 성공 여부 관찰
        viewModel.uploadStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "업로드 성공!", Toast.LENGTH_SHORT).show() // 성공 메시지
                clearInputs() // 입력 필드 초기화
            }
        }

        // 에러 메시지 관찰
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show() // 에러 메시지 표시
        }
    }

    // 이미지 선택기 실행
    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*" // 이미지 타입 필터 설정
        }
        imagePickerLauncher.launch(intent)
    }

    // 이미지 선택 결과 처리
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data // 선택된 이미지의 Uri 저장
                binding?.uploadImageview?.setImageURI(selectedImageUri) // 선택된 이미지 표시
            }
        }

    // 입력 필드 초기화
    private fun clearInputs() {
        binding?.uploadImageview?.setImageResource(0) // 이미지 초기화
        binding?.addphotoEditEdittext?.text?.clear() // 설명 초기화
        selectedImageUri = null // 선택된 이미지 초기화
    }

    // 한국어 포함 여부를 검사하는 함수
    private fun containsKorean(text: String): Boolean {
        return text.any { it in '\uAC00'..'\uD7A3' } // 한국어 유니코드 범위에 해당하는 문자가 있는지 검사
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null // ViewBinding 정리
    }
}




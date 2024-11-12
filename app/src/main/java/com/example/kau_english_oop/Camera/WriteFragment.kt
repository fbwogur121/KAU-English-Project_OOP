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
    var auth = FirebaseAuth.getInstance()
    var firestore=FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()

    private var photoUri: Uri? = null
    private val photoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            photoUri = result.data?.data
            binding?.uploadImageview?.setImageURI(photoUri)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteBinding.inflate(inflater, container, false)

        // 사진 업로드 버튼 클릭 리스너
        binding?.addphotoUploadBtn?.setOnClickListener {
            contentUpload()
        }

        // 사진 선택 Intent 실행
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        binding?.addPhotoBtn?.setOnClickListener {
            photoResult.launch(intent)
        }

        return binding?.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    private fun contentUpload() {
        var time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        var imageFileName = "IMAGE_$time.png"
        var storagePath = storage.reference.child("images").child(imageFileName)

        // Firebase Storage에 파일 업로드
        photoUri?.let {
            storagePath.putFile(it).continueWithTask { storagePath.downloadUrl }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        var contentModel=ContentModel().apply{
                            imageUrl=downloadUrl.toString()
                            explain=binding?.addphotoEditEdittext?.text.toString()
                            uid=auth.uid
                            userId=auth.currentUser?.email
                            timestamp= System.currentTimeMillis()

                        }
                        firestore.collection("images").document().set(contentModel).addOnSuccessListener {
                            Toast.makeText(requireContext(), "업로드 성공: $downloadUrl", Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Toast.makeText(requireContext(), "업로드 실패", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

}



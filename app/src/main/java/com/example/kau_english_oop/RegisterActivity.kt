package com.example.kau_english_oop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kau_english_oop.databinding.ActivityRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Google Sign-In 옵션 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Login 버튼 클릭 시
        binding.loginButton.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // 이미 로그인된 경우 Firestore에서 사용자 정보 확인
                checkUserInDatabase(currentUser.uid)
            } else {
                Toast.makeText(this, getString(R.string.login_required), Toast.LENGTH_SHORT).show()
            }
        }

        // Join 버튼 클릭 시
        binding.joinButton.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // 이메일로 사용자 존재 여부 확인
                checkIfUserExists(currentUser.email ?: "")
            } else {
                // Google 로그인 진행
                signIn()
            }
        }

        // Activity Result Launcher 초기화
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w("RegisterActivity", "Google sign in failed", e)
                    Toast.makeText(this, getString(R.string.google_sign_in_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        checkIfUserExists(auth.currentUser?.email ?: "")
                    } else {
                        Toast.makeText(this, getString(R.string.user_info_unavailable), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.w("RegisterActivity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, getString(R.string.firebase_auth_failed), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkIfUserExists(email: String) {
        db.collection("users").whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // 기존 사용자 존재 -> 로그인 화면으로 전환
                    Toast.makeText(this, getString(R.string.existing_account_login), Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    // 신규 사용자 -> 회원가입 진행
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        saveUserToDatabase(userId)
                    } else {
                        Toast.makeText(this, getString(R.string.user_info_unavailable), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("RegisterActivity", "Firestore 조회 실패: ${exception.message}")
                Toast.makeText(this, getString(R.string.user_check_failed), Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToDatabase(userId: String) {
        val user = auth.currentUser
        val userMap = hashMapOf(
            "uid" to userId,
            "email" to user?.email,
            "displayName" to user?.displayName
        )

        db.collection("users").document(userId).set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            }
            .addOnFailureListener { exception ->
                Log.w("RegisterActivity", "Firestore 저장 실패: ${exception.message}")
                Toast.makeText(this, getString(R.string.database_save_failed), Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUserInDatabase(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // 사용자 정보가 있는 경우 MainActivity로 이동
                    navigateToMainActivity()
                } else {
                    // 사용자 정보가 없는 경우 메시지 표시
                    Toast.makeText(this, getString(R.string.user_info_missing), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("RegisterActivity", "Firestore 조회 실패: ${exception.message}")
                Toast.makeText(this, getString(R.string.database_fetch_failed), Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

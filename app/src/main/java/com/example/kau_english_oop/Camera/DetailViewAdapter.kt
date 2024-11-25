package com.example.kau_english_oop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kau_english_oop.databinding.ItemDetailBinding

// RecyclerView 어댑터
class DetailViewAdapter(
    private var imageList: List<Map<String, Any>>, // Firestore에서 가져온 데이터 리스트
    private val viewModel: DetailViewViewModel // 좋아요 상태 업데이트를 위해 ViewModel 사용
) : RecyclerView.Adapter<DetailViewAdapter.ViewHolder>() {

    // ViewBinding을 사용하는 ViewHolder
    class ViewHolder(val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ViewBinding을 초기화하여 ViewHolder 생성
        val binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = imageList[position] // 현재 위치의 데이터를 가져옴
        val profileUrl = data["profileImage"] as? String ?: "" // Firestore에서 프로필 이미지 URL
        val contentUrl = data["imageUrl"] as? String ?: "" // 콘텐츠 이미지 URL
        val explanation = data["explanation"] as? String ?: "No explanation" // 설명
        val email = data["email"] as? String ?: "Unknown" // 이메일
        val timestamp = data["timestamp"] as? Long ?: 0  // 업로드 시간
        val documentId = data["documentId"] as? String // Firestore 문서 ID
        var isLiked = data["like"] as? Boolean ?: false // 좋아요 상태 확인

        // ViewBinding을 통해 데이터 연결
        with(holder.binding) {
            // 프로필 이미지 로드
            Glide.with(profileImageview.context)
                .load(profileUrl)
                .circleCrop()
                .into(profileImageview)

            // 콘텐츠 이미지 로드
            Glide.with(contentImageview.context)
                .load(contentUrl)
                .into(contentImageview)

            // 설명 및 이메일 설정
            profileTextview.text = email
            explainTextview.text = explanation

            // 타임스탬프를 포맷해서 표시
            val date =
                java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                    .format(java.util.Date(timestamp))
            timestampTextview.text = date

            // 좋아요 상태에 따라 이미지 설정
            favoriteImageview.setImageResource(if (isLiked) R.drawable.ic_liked else R.drawable.ic_unliked)

            // 좋아요 버튼 클릭 이벤트
            favoriteImageview.setOnClickListener {
                if (documentId != null) {
                    // Firestore에서 like 상태 업데이트
                    isLiked = !isLiked // 좋아요 상태 반전
                    viewModel.updateLikeStatus(documentId, isLiked) // ViewModel에서 Firestore 업데이트
                    // 좋아요 상태에 따라 아이콘 업데이트
                    favoriteImageview.setImageResource(if (isLiked) R.drawable.ic_liked else R.drawable.ic_unliked)
                }
            }
        }
    }

    override fun getItemCount(): Int = imageList.size // 데이터 리스트 크기 반환

    // RecyclerView 데이터를 업데이트하고 화면 새로고침
    fun updateData(newData: List<Map<String, Any>>) {
        imageList = newData // 새로운 데이터로 교체
        notifyDataSetChanged() // RecyclerView 새로고침
    }
}

package com.example.kau_english_oop.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.R
import com.squareup.picasso.Picasso // Picasso 라이브러리 추가

class ConversationAdapter(private var conversations: MutableList<Pair<String, String>>) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    // ViewHolder 클래스 정의
    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val conversationTextView: TextView = itemView.findViewById(R.id.conversationTextView) // 대화 내용을 표시할 TextView
        // 대화와 관련된 건물 이미지를 표시할 ImageView
        val conversationImageView: ImageView = itemView.findViewById(R.id.conversationImageView) // 이미지 뷰 추가
    }

    // ViewHolder를 생성하는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        // item_conversation.xml 레이아웃을 인플레이트하여 ViewHolder 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    // ViewHolder에 데이터를 바인딩하는 메서드
    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        // 대화 텍스트를 ViewHolder의 TextView에 설정
        holder.conversationTextView.text = conversations[position].first
        val imageUrl = conversations[position].second
        if (imageUrl.isNotEmpty()) {
            Picasso.get().load(imageUrl).into(holder.conversationImageView) // 이미지 로드
            holder.conversationImageView.visibility = View.VISIBLE // 이미지 보이기
        } else { // 이미지 URL이 비어 있는 경우 ImageView 숨기기
            holder.conversationImageView.visibility = View.GONE
        }
    }

    // RecyclerView의 항목 수를 반환하는 메서드
    override fun getItemCount(): Int {
        return conversations.size
    }

    // 대화 목록을 업데이트하는 메서드
    fun updateConversations(newConversations: List<Pair<String, String>>) {
        conversations.clear() // 기존 대화 목록 지우기
        conversations.addAll(newConversations) // 새로운 대화 목록 추가
        notifyDataSetChanged() // RecyclerView에 데이터 변경 알리기
    }
}
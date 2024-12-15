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

    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val conversationTextView: TextView = itemView.findViewById(R.id.conversationTextView)
        val conversationImageView: ImageView = itemView.findViewById(R.id.conversationImageView) // 이미지 뷰 추가
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.conversationTextView.text = conversations[position].first
        val imageUrl = conversations[position].second
        if (imageUrl.isNotEmpty()) {
            Picasso.get().load(imageUrl).into(holder.conversationImageView) // 이미지 로드
            holder.conversationImageView.visibility = View.VISIBLE // 이미지 보이기
        } else {
            holder.conversationImageView.visibility = View.GONE // 이미지 숨기기
        }
    }

    override fun getItemCount(): Int {
        return conversations.size
    }

    fun updateConversations(newConversations: List<Pair<String, String>>) {
        conversations.clear()
        conversations.addAll(newConversations)
        notifyDataSetChanged()
    }
}

package com.example.kau_english_oop.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.R

class ConversationAdapter(private var conversations: MutableList<String>) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val conversationTextView: TextView = itemView.findViewById(R.id.conversationTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.conversationTextView.text = conversations[position]
    }

    override fun getItemCount(): Int {
        return conversations.size
    }

    fun updateConversations(newConversations: List<String>) {
        conversations.clear()
        conversations.addAll(newConversations)
        notifyDataSetChanged()
    }
}
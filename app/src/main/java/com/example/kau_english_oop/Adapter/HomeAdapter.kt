package com.example.kau_english_oop.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.R

class HomeAdapter(private var imageList: List<Int>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageButton: ImageButton = view.findViewById(R.id.imageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageButton.setImageResource(imageList[position])
        // TODO: 클릭 리스너 추가 해야함
    }

    override fun getItemCount() = imageList.size

    // 이미지 리스트 업데이트 메서드 추가
    fun updateImageList(newImageList: List<Int>) {
        imageList = newImageList
        notifyDataSetChanged() // 데이터 변경 알림
    }
}
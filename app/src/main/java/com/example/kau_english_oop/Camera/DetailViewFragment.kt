package com.example.kau_english_oop.Camera

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.kau_english_oop.R
import com.example.kau_english_oop.databinding.FragmentDetailViewBinding
import com.example.kau_english_oop.databinding.ItemDetailBinding
import com.example.kau_english_oop.model.ContentModel
import com.google.firebase.firestore.FirebaseFirestore

class DetailViewFragment : Fragment() {

    lateinit var binding: FragmentDetailViewBinding // View Binding을 위한 binding 변수 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentDetailViewBinding.inflate(inflater,container,false)


        // RecyclerView의 어댑터를 설정하여 DetailViewRecyclerViewAdapter로 설정
        binding.detailviewRecyclerveiw.adapter = DetailViewRecyclerViewAdapter()
        // RecyclerView의 레이아웃 매니저를 LinearLayoutManager로 설정하여 세로 방향 리스트로 표시
        binding.detailviewRecyclerveiw.layoutManager = LinearLayoutManager(activity)
        return binding.root // binding의 루트 뷰 반환
    }

    // RecyclerView의 각 항목에 대한 뷰 홀더 클래스
    inner class DetailViewHolder(var binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root)

    // RecyclerView 어댑터 클래스 정의
    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<DetailViewHolder>() {
        var firestore = FirebaseFirestore.getInstance() // Firestore 인스턴스 생성
        var contentModels = arrayListOf<ContentModel>() // ContentModel을 저장할 리스트 생성

        init {
            // Firestore의 "images" 컬렉션에 대한 SnapshotListener를 설정
            firestore.collection("images").addSnapshotListener { value, error ->
                contentModels.clear() // 기존 리스트 초기화
                for (item in value!!.documents) { // Firestore에서 가져온 문서 반복
                    var contentModel = item.toObject(ContentModel::class.java) // 문서를 ContentModel 객체로 변환
                    contentModels.add(contentModel!!) // 변환된 객체를 리스트에 추가
                }
                notifyDataSetChanged() // 데이터 변경을 RecyclerView에 알림
            }
        }

        // 뷰 홀더가 생성될 때 호출
        @SuppressLint("SuspiciousIndentation")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
            // ItemDetailBinding을 통해 뷰를 inflate하여 DetailViewHolder 생성
            var view = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DetailViewHolder(view)
        }

        // RecyclerView의 항목 수 반환
        override fun getItemCount(): Int {
            return contentModels.size
        }

        // 뷰 홀더가 데이터와 결합될 때 호출
        override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
            var contentModel = contentModels[position] // 현재 위치의 ContentModel 가져오기
            holder.binding.profileTextview.text = contentModel.userId // 사용자 ID를 profileTextView에 설정
            holder.binding.explainTextview.text = contentModel.explain // 설명 텍스트를 explainTextView에 설정
            holder.binding.likeTextview.text = "Like" + contentModel.favoriteCount // 좋아요 수를 likeTextView에 설정
            // Glide 라이브러리를 사용하여 이미지 URL을 contentImageView에 로드
            Glide.with(holder.itemView.context).load(contentModel.imageUrl).into(holder.binding.contentImageview)
        }
    }
}

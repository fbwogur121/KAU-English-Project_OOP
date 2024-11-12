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

/**
 * A simple [Fragment] subclass.
 * Use the [DetailViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentDetailViewBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_detail_view, container, false)

        binding.detailviewRecyclerveiw.adapter=DetailViewRecyclerViewAdapter()
        binding.detailviewRecyclerveiw.layoutManager= LinearLayoutManager(activity)
        return binding.root
    }



    inner class DetailViewHolder(var binding: ItemDetailBinding): RecyclerView.ViewHolder(binding.root)
    inner class DetailViewRecyclerViewAdapter(): RecyclerView.Adapter<DetailViewHolder>(){
        var firestore= FirebaseFirestore.getInstance()
        var contentModels= arrayListOf<ContentModel>()
        init{
            firestore.collection("images").addSnapshotListener{value,error ->

                contentModels.clear()
                for(item in value!!.documents){
                    var contentModel=item.toObject(ContentModel::class.java)
                    contentModels.add(contentModel!!)

                }
                notifyDataSetChanged()
            }
        }

        @SuppressLint("SuspiciousIndentation")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
          var view= ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DetailViewHolder(view)
        }

        override fun getItemCount(): Int {
            return contentModels.size
        }

        override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
            var contentModel=contentModels[position]
            holder.binding.profileTextview.text=contentModel.userId
            holder.binding.explainTextview.text=contentModel.explain
            holder.binding.likeTextview.text="like"+contentModel.favoriteCount
            Glide.with(holder.itemView.context).load(contentModel.imageUrl).into(holder.binding.contentImageview)
        }
    }

}
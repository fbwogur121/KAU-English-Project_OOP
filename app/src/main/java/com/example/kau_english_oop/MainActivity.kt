////package com.example.kau_english_oop
////
////import android.os.Bundle
////import androidx.activity.enableEdgeToEdge
////import androidx.appcompat.app.AppCompatActivity
////import androidx.core.view.ViewCompat
////import androidx.core.view.WindowInsetsCompat
////
////class MainActivity : AppCompatActivity() {
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        enableEdgeToEdge()
////        setContentView(R.layout.activity_main)
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
////            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
////            insets
////        }
////    }
////}
//package com.example.kau_english_oop
//import android.content.Intent
//import com.example.kau_english_oop.R
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.kau_english_oop.databinding.ActivityMainBinding
//import com.google.android.material.navigation.NavigationBarView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // BottomNavigationView 설정
//        binding.bottomNavigation.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_map -> {
//                    startActivity(Intent(this, LocationBasedPhrasesActivity::class.java))
//                    true
//                }
//                R.id.navigation_journal -> { true }
//                R.id.navigation_gallery -> { true }
//                R.id.navigation_calendar -> { true }
//                else -> false
//            }
//        }
//        // RecyclerView 설정
//        binding.interestRecyclerview.layoutManager = LinearLayoutManager(this)
//        binding.interestRecyclerview.adapter = InterestAdapter(listOf(
//            "Travel", "Celebrity", "Game", "Animal", "Science", "Finance"
//        ))
//    }
//}
//
//class InterestAdapter(private val interests: List<String>) : RecyclerView.Adapter<InterestAdapter.ViewHolder>() {
//    private val selectedInterests = mutableSetOf<String>()
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textView: TextView = itemView.findViewById(android.R.id.text1)
//        val starButton: ImageButton = itemView.findViewById(R.id.star_button)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.interest_item, parent, false)
//        return ViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val interest = interests[position]
//        holder.textView.text = interest
//        holder.starButton.setImageResource(if (interest in selectedInterests) R.drawable.ic_star else R.drawable.ic_star_border)
//        holder.starButton.setOnClickListener {
//            if (interest in selectedInterests) {
//                selectedInterests.remove(interest)
//            } else {
//                selectedInterests.add(interest)
//            }
//            notifyDataSetChanged() // 선택 상태 변경 후 업데이트
//        }
//    }
//
//    override fun getItemCount(): Int = interests.size
//}
//
package com.example.kau_english_oop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_map -> {
                    startActivity(Intent(this, LocationBasedPhrasesActivity::class.java))
                    true
                }
                R.id.navigation_journal -> true
                R.id.navigation_gallery -> true
                R.id.navigation_calendar -> true
                else -> false
            }
        }

        // RecyclerView 설정
        binding.interestRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.interestRecyclerview.adapter = InterestAdapter(listOf(
            "Travel", "Celebrity", "Game", "Animal", "Science", "Finance"
        ))
    }
}


class InterestAdapter(private val interests: List<String>) :
    RecyclerView.Adapter<InterestAdapter.ViewHolder>() {
    private val selectedInterests = mutableSetOf<String>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
        val starButton: ImageButton = itemView.findViewById(R.id.star_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.interest_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val interest = interests[position]
        holder.textView.text = interest
        holder.starButton.setOnClickListener {
            if (interest in selectedInterests) {
                selectedInterests.remove(interest)
            } else {
                selectedInterests.add(interest)
            }
            notifyDataSetChanged()
        }
        // 이미지 설정 부분 수정
        holder.starButton.setImageResource(if (interest in selectedInterests) R.drawable.ic_star else R.drawable.ic_star_border)
    }

    override fun getItemCount(): Int = interests.size
}

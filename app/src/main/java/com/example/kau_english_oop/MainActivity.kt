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
        val textView: TextView = itemView.findViewById(R.id.text1)  // android.R.id.text1 -> R.id.text1
        val starButton: ImageButton = itemView.findViewById(R.id.star_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.interest_item, parent, false)
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
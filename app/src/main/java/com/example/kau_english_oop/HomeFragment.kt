
package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.Adapter.MyAdapter
import com.example.kau_english_oop.Adapter.SectionItem
import com.example.kau_english_oop.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter // 어댑터 클래스 추가

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        recyclerView = binding?.recyclerView ?: return
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MyAdapter(getData()) // 데이터 가져오기
        recyclerView.adapter = adapter

        // btnMenu 클릭 리스너 추가
        binding?.btnMenu?.setOnClickListener {
            // MenuFragment로 전환
            val menuFragment = MenuFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, menuFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun getData(): List<SectionItem> {
        return listOf(
            SectionItem("My account", "Name\nDescription"),
            SectionItem("My Interest", "Science\nJournal\nYouTube"),
            SectionItem("My Favorite Post", "Title\nTitle"),
            SectionItem("My Point", "350 point")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
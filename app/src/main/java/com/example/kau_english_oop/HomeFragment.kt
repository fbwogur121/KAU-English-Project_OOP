
package com.example.kau_english_oop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.Adapter.ImageButtonAdapter
import com.example.kau_english_oop.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageButtonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding!!.recyclerView
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ImageButtonAdapter(getImageButtonList()) // 이미지 버튼 리스트 가져오기
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

    private fun getImageButtonList(): List<Int> {
        return listOf(
            R.drawable.lexilearn,
            R.drawable.journal_button,
            R.drawable.youtube_button,
            R.drawable.today_button,
            R.drawable.shopping_button
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
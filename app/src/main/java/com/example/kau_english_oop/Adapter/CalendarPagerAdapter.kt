package com.example.kau_english_oop.Adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.CalendarAdapter

class CalendarPagerAdapter(
    private val months: List<List<String>>, // 월별 날짜 리스트
    private val checkedDates: MutableList<String>,
    private val onDateClick: (String) -> Unit
) : RecyclerView.Adapter<CalendarPagerAdapter.MonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val recyclerView = RecyclerView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutManager = GridLayoutManager(parent.context, 7) // 7열 그리드
        }
        return MonthViewHolder(recyclerView)
    }


    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val adapter = CalendarAdapter(checkedDates, onDateClick)
        adapter.submitList(months[position]) // position에 해당하는 월의 데이터만 전달
        holder.recyclerView.adapter = adapter
    }

    override fun getItemCount(): Int = months.size

    class MonthViewHolder(val recyclerView: RecyclerView) : RecyclerView.ViewHolder(recyclerView)
}

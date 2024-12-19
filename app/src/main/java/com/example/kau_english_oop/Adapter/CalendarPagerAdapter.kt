// CalendarPagerAdapter.kt
package com.example.kau_english_oop.Adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.CalendarAdapter

class CalendarPagerAdapter(
    private val monthlyDates: List<List<String>>,
    private val selectedDates: MutableList<String>,
    private val onDateClick: (String) -> Unit
) : RecyclerView.Adapter<CalendarPagerAdapter.MonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val recyclerView = RecyclerView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutManager = GridLayoutManager(parent.context, 7)
        }
        return MonthViewHolder(recyclerView)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val adapter = CalendarAdapter(selectedDates, onDateClick)
        adapter.updateDates(monthlyDates[position])
        holder.recyclerView.adapter = adapter
    }

    override fun getItemCount(): Int = monthlyDates.size

    class MonthViewHolder(val recyclerView: RecyclerView) : RecyclerView.ViewHolder(recyclerView)
}

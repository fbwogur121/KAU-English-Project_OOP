package com.example.kau_english_oop

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.databinding.ItemCalendarDateBinding

class CalendarAdapter(
    private val checkedDates: MutableList<String>, // 체크된 날짜 리스트
    private val onDateClick: (String) -> Unit // 날짜 클릭 이벤트 콜백
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private val dates = mutableListOf<String>() // 캘린더 날짜 리스트

    // 날짜 리스트 업데이트
    fun submitList(newDates: List<String>) {
        dates.clear()
        dates.addAll(newDates)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarDateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val date = dates[position]
        Log.d("CalendarAdapter", "Binding date: $date at position $position")
        holder.bind(date, checkedDates.contains(date))
    }

    override fun getItemCount(): Int = dates.size

    inner class CalendarViewHolder(private val binding: ItemCalendarDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: String, isChecked: Boolean) {
            binding.textDate.text = date

            // 체크된 날짜는 배경 변경
            if (isChecked) {
                binding.textDate.setBackgroundResource(R.drawable.checked_date_background)
            } else {
                binding.textDate.setBackgroundResource(android.R.color.transparent)
            }

            // 날짜 클릭 이벤트
            binding.textDate.setOnClickListener {
                onDateClick(date)
            }
        }
    }
}

// CalendarAdapter.kt
package com.example.kau_english_oop

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kau_english_oop.databinding.ItemCalendarDateBinding

class CalendarAdapter(
    private val selectedDates: MutableList<String>,
    private val onDateClick: (String) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private val dates = mutableListOf<String>()

    fun updateDates(newDates: List<String>) {
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
        val isSelected = selectedDates.any { it.trim() == date.trim() }
        Log.d("CalendarAdapter", "Binding date: $date, isSelected: $isSelected")
        holder.bind(date, isSelected)
    }

    override fun getItemCount(): Int = dates.size

    inner class CalendarViewHolder(private val binding: ItemCalendarDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: String, isSelected: Boolean) {
            val displayDate = date.substring(8, 10)
            binding.textDate.text = displayDate

            if (isSelected) {
                binding.textDate.setBackgroundResource(R.drawable.checked_date_background)
            } else {
                binding.textDate.setBackgroundResource(android.R.color.transparent)
            }

            binding.textDate.setOnClickListener {
                onDateClick(date)
            }
        }
    }
}

package com.bangkit.pedulibumil.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.pedulibumil.R
import java.text.DateFormat
import java.util.Date

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val historyList = mutableListOf<HistoryEntity>()

    fun setData(newData: List<HistoryEntity>) {
        historyList.clear()
        historyList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = historyList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRiskCategory: TextView = itemView.findViewById(R.id.tvRiskCategory)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)

        fun bind(history: HistoryEntity) {
            tvRiskCategory.text = history.riskCategory
            tvTimestamp.text = DateFormat.getDateTimeInstance().format(Date(history.timestamp))
        }
    }
}

package com.fdhasna21.latihansqlitegmaps_activitytracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.record_row.view.*

class HistoryAdapter(val arrayList: ArrayList<HistoryModel>, val context: Context)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val layoutRecord = itemView.record_layout
        val posRecord =  itemView.record_no
        val timestampRecord = itemView.record_time
        val historyRecord = itemView.record_activity
        val addressRecord = itemView.record_address
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.record_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arrayList.get(position)
        holder.posRecord.text = (position+1).toString()
        holder.timestampRecord.text = """${item.date}
                                        |${item.time}""".trimMargin()
        holder.historyRecord.text = item.history
        holder.addressRecord.text = item.address

        if(position%2 == 0) {
            holder.layoutRecord.setBackgroundColor(ContextCompat.getColor(context,R.color.pink_lighter))
        }
        else{
            holder.layoutRecord.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}
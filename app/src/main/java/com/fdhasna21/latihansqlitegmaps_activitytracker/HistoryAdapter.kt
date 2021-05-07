package com.fdhasna21.latihansqlitegmaps_activitytracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.record_row2.view.*

class HistoryAdapter(val arrayList: ArrayList<HistoryModel>, val context: Context)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val layoutRecord = itemView.record_layout
        val historyRecord = itemView.record_activity
        val addressRecord = itemView.record_address

        //for layout record_row.xml
//        val posRecord =  itemView.record_no
//        val timestampRecord = itemView.record_time

        //for layout record_row2.xml
        val moreRecord = itemView.record_more
        val dateRecord = itemView.record_date
        val timeRecord = itemView.record_time
        val deleteRecord = itemView.record_btnDelete
        val buttonContainerRecord = itemView.record_button_container
        val dateTimeContainerRecord = itemView.record_datetime_container
        var openMoreRecord = true

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.record_row2, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arrayList.get(position)
        holder.historyRecord.text = item.history
        holder.addressRecord.text = item.address
        if(position%2 == 0) {
            holder.layoutRecord.setBackgroundColor(ContextCompat.getColor(context,R.color.pink_lighter))
        }
        else{
            holder.layoutRecord.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        //for layout record_row.xml
//        holder.posRecord.text = (position+1).toString()
//        holder.timestampRecord.text = """${item.date}
//                                        |${item.time}""".trimMargin()

        //for layout record_row2.xml
        holder.dateRecord.text = item.date
        holder.timeRecord.text = item.time
        holder.addressRecord.visibility = View.GONE
        holder.dateTimeContainerRecord.visibility = View.GONE
        holder.buttonContainerRecord.visibility = View.GONE
        holder.moreRecord.setOnClickListener {
            if(holder.openMoreRecord){
                holder.addressRecord.visibility = View.VISIBLE
                holder.dateTimeContainerRecord.visibility = View.VISIBLE
                holder.buttonContainerRecord.visibility = View.VISIBLE
                holder.moreRecord.setImageResource(R.drawable.ic_dropup)
                holder.openMoreRecord = false
            }
            else{
                holder.addressRecord.visibility = View.GONE
                holder.dateTimeContainerRecord.visibility = View.GONE
                holder.buttonContainerRecord.visibility = View.GONE
                holder.moreRecord.setImageResource(R.drawable.ic_dropdown)
                holder.openMoreRecord = true
            }
        }

        holder.deleteRecord.setOnClickListener{
            if(context is TrackerHistory){
                context.deleteRecord(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}
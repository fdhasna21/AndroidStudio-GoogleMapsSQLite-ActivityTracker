package com.fdhasna21.latihansqlitegmaps_activitytracker

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_tracker_history.*

class TrackerHistory : AppCompatActivity() {
    fun deleteRecord(history: HistoryModel){
        val builder = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        builder.setTitle("Delete Record")
        builder.setMessage("Are you sure?")
        builder.setCancelable(false)
        builder.setIcon(R.drawable.ic_warning)

        builder.setPositiveButton("Yes"){ dialog: DialogInterface, which ->
            val databaseHandler = DatabaseHandler(this)
            databaseHandler.deleteHistory(history)
            Toast.makeText(this,"Record deleted.", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            showHistory()
        }

        builder.setNegativeButton("No"){ dialog: DialogInterface, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun getOutputArray():ArrayList<HistoryModel>{
        val databaseHandler = DatabaseHandler(this)
        val dataOut = databaseHandler.showHistory()
        history_recyclerView.layoutManager = LinearLayoutManager(this)
        history_recyclerView.adapter = HistoryAdapter(dataOut, this)
        return dataOut
    }

    private fun showHistory(){
        val outputArray = getOutputArray()
        if (outputArray.size > 0) {
            txt_noRecords.visibility = View.GONE
            history_recyclerView.visibility = View.VISIBLE
        } else {
            txt_noRecords.visibility = View.VISIBLE
            history_recyclerView.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker_history)
        supportActionBar!!.setTitle("History")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        showHistory()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

}
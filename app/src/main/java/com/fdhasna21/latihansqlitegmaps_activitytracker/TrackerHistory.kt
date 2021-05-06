package com.fdhasna21.latihansqlitegmaps_activitytracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_tracker_history.*

class TrackerHistory : AppCompatActivity() {
    private fun getOutputArray():ArrayList<HistoryModel>{
        val databaseHandler = DatabaseHandler(this)
        val dataOut = databaseHandler.showHistory()
        history_recyclerView.layoutManager = LinearLayoutManager(this)
        history_recyclerView.adapter = HistoryAdapter(dataOut, this)
        return dataOut
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracker_history)
        supportActionBar!!.setTitle("History")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val outputArray = getOutputArray()
        if (outputArray.size > 0) {
            txt_noRecords.visibility = View.GONE
            history_recyclerView.visibility = View.VISIBLE
        } else {
            txt_noRecords.visibility = View.VISIBLE
            history_recyclerView.visibility = View.GONE

        }
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
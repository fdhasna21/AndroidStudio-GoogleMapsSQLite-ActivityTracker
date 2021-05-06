package com.fdhasna21.latihansqlitegmaps_activitytracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException

class DatabaseHandler(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private val DATABASE_NAME = "HistoryDatabase"
        private val DATABASE_VERSION = 1

        private val TABLE_NAME = "HistoryTable"
        private val KEY_ID = "id"
        private val KEY_DATE = "date"
        private val KEY_TIME = "time"
        private val KEY_ACTIVITY = "activity"
        private val KEY_ADDRESS = "address"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY, $KEY_DATE TEXT, $KEY_TIME TEXT, $KEY_ACTIVITY TEXT, $KEY_ADDRESS TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addHistory(history:HistoryModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_DATE, history.date)
        contentValues.put(KEY_TIME, history.time)
        contentValues.put(KEY_ACTIVITY, history.history)
        contentValues.put(KEY_ADDRESS, history.address)

        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }

    fun showHistory(): ArrayList<HistoryModel> {
        val db = this.writableDatabase
        val tempDiary = ArrayList<HistoryModel>()
        val sqlCommad = "SELECT * FROM $TABLE_NAME;"

        var cursor : Cursor? = null
        try{
            cursor = db.rawQuery(sqlCommad, null)
        }
        catch (e: SQLException){
            db.execSQL(sqlCommad)
            return arrayListOf()
        }

        if(cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val date = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                val time = cursor.getString(cursor.getColumnIndex(KEY_TIME))
                val activity = cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY))
                val address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS))
                tempDiary.add(HistoryModel(id, date, time, activity, address))
            }while(cursor.moveToNext())
        }
        cursor.close()
        return tempDiary
    }

    fun deleteHistory(history: HistoryModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, history.id)

        val success = db.delete(TABLE_NAME, "$KEY_ID = ${history.id}", null)
        db.close()
        return success
    }
}
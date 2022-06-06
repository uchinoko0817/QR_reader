package com.example.qrreader

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HistoryService (context: Context){

    // Helper
    private val openHelper:SQLiteOpenHelper

    // Constructor
    init {
        openHelper = DatabaseHelper(context)
    }

    // Save single record
    fun saveData(uri :String, createdAt :ZonedDateTime){
        val db = openHelper.writableDatabase
        val data = ContentValues()
        data.put(DatabaseHelper.COLUMN_NAME_URI, uri)
        data.put(DatabaseHelper.COLUMN_NAME_CREATED_AT, getDateTimeText(createdAt))
        db.insert(DatabaseHelper.TABLE_NAME, null, data)
    }

    // Read All records
    fun readAll(): ArrayList<History>{
        val db = openHelper.readableDatabase
        val cursor = db.rawQuery(DatabaseHelper.SQL_SELECT_ALL, null)
        val list = arrayListOf<History>()
        if(cursor.count > 0){
            cursor.moveToFirst()
            while(!cursor.isAfterLast){
                list.add(History(cursor.getInt(0), cursor.getString(1), ZonedDateTime.parse(cursor.getString(2))))
                cursor.moveToNext()
            }
        }
        return list
    }

    // Close DB
    fun close(){
        openHelper.close()
    }

    // Convert ZDT to String
    private fun getDateTimeText(dateTime: ZonedDateTime):String{
        return dateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    }
}
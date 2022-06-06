package com.example.qrreader

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        // DB version
        const val DATABASE_VERSION = 1
        // DB name
        const val DATABASE_NAME = "QRReaderDB.db"
        // Table name
        const val TABLE_NAME = "history"
        // Column names
        const val _ID = "_id"
        const val COLUMN_NAME_CREATED_AT = "created_at"
        const val COLUMN_NAME_URI = "uri"
        // SQL Create table
        const val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_NAME ($_ID INTEGER PRIMARY KEY, $COLUMN_NAME_URI TEXT, $COLUMN_NAME_CREATED_AT TEXT)"
        // SQL Delete table
        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
        // SQL Select all
        const val SQL_SELECT_ALL = "SELECT $_ID, $COLUMN_NAME_URI, $COLUMN_NAME_CREATED_AT FROM $TABLE_NAME ORDER BY $_ID DESC"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}
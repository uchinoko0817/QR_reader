package com.example.qrreader

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

class HistoryActivity : AppCompatActivity(){

    // DB service
    private val historyService = HistoryService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Make list view
        try{
            val histories = historyService.readAll()
            val listView = findViewById<ListView>(R.id.list_view)
            val adapter = HistoryListAdapter(this, histories)
            listView.adapter = adapter
        }
        catch(e: Exception){
            Log.e("readAll", e.toString())
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Failed to read data")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        historyService.close()
        super.onDestroy()
    }
}
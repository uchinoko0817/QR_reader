package com.example.qrreader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class HistoryListAdapter(private val context: Context, private val histories:ArrayList<History>):BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.history_item, null)
        val textView = view.findViewById<TextView>(R.id.text_view)
        textView.text = histories[position].uri
        return view
    }

    override fun getItemId(position: Int): Long {
        return histories[position].id.toLong()
    }

    override fun getItem(position: Int): Any {
        return histories[position]
    }

    override fun getCount(): Int {
        return histories.size
    }
}
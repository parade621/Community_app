package com.parade621.community_app.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.parade621.community_app.R

class BoardLVAdapter(val boardList : MutableList<BoardModel>):BaseAdapter() {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.board_lv_items,parent,false)
        }

        convertView!!.findViewById<TextView>(R.id.LVTitle).text = boardList[position].title
        convertView!!.findViewById<TextView>(R.id.LVContent).text = boardList[position].content
        convertView!!.findViewById<TextView>(R.id.LVTime).text = boardList[position].time


        return convertView!!
    }
}
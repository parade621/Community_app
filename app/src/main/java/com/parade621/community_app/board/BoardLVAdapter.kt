package com.parade621.community_app.board

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.parade621.community_app.R
import com.parade621.community_app.utils.FBAuth

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
        //if(convertView == null){
        convertView = LayoutInflater.from(parent?.context).inflate(R.layout.board_lv_items,parent,false)
        //}

        // 작성자 본인이면, item의 색이 좀 다르게 표시되도록 할 것임.
        val boardItemView = convertView!!.findViewById<LinearLayout>(R.id.BoarditemView)
        if(boardList[position].uid.equals(FBAuth.getUid())) {
            boardItemView.setBackgroundColor(Color.parseColor("#fff0f5"))
        }

        convertView!!.findViewById<TextView>(R.id.LVTitle).text = boardList[position].title
        convertView!!.findViewById<TextView>(R.id.LVContent).text = boardList[position].content
        convertView!!.findViewById<TextView>(R.id.LVTime).text = boardList[position].time


        return convertView!!
    }
}
package com.parade621.community_app.board

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.parade621.community_app.R
import com.parade621.community_app.utils.FBAuth
import java.util.logging.Handler

class CommentLVAdapter(val commentList : MutableList<CommentModel>): BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        var convertView = convertView
        //if(convertView == null){
        convertView = LayoutInflater.from(parent?.context).inflate(R.layout.board_comment_lv_item,parent,false)
        //}

        // 작성자 본인이면, item의 색이 좀 다르게 표시되도록 할 것임.
        val commentItemView = convertView!!.findViewById<LinearLayout>(R.id.boardCommentItemView)
        if(commentList[position].uid.equals(FBAuth.getUid())) {
            //commentItemView.setBackgroundColor(Color.parseColor("#fff0f5"))
            commentItemView.setBackgroundResource(R.drawable.comment_uid_shape)
            commentItemView.findViewById<TextView>(R.id.editBtn).isVisible=true
            commentItemView.findViewById<TextView>(R.id.deleteBtn).isVisible=true
        }
        convertView!!.findViewById<TextView>(R.id.LVTitle).text = commentList[position].uid
        convertView!!.findViewById<TextView>(R.id.LVContent).text = commentList[position].comment
        convertView!!.findViewById<TextView>(R.id.LVTime).text = commentList[position].time

        return convertView!!
    }
}
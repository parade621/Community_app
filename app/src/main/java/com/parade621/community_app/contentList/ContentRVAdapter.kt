package com.parade621.community_app.contentList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parade621.community_app.R

class ContentRVAdapter(val items: ArrayList<String>):RecyclerView.Adapter<ContentRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ContentRVAdapter.ViewHolder{

        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_items, parent,false)

        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ContentRVAdapter.ViewHolder, position:Int){
        holder.bindItems(items[position])
    }
    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindItems(item : String){


        }
    }


}
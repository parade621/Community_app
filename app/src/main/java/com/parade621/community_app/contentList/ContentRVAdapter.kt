package com.parade621.community_app.contentList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parade621.community_app.R

class ContentRVAdapter(val context : Context, val items: ArrayList<ContentModel>):RecyclerView.Adapter<ContentRVAdapter.ViewHolder>() {

    interface ItemClick{
        fun onClick(view : View, position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ContentRVAdapter.ViewHolder{

        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_items, parent,false)

        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ContentRVAdapter.ViewHolder, position:Int){
        if(itemClick !=null){
            holder.itemView.setOnClickListener { v->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(items[position])
    }
    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindItems(item : ContentModel){

            val ContentTitle = itemView.findViewById<TextView>(R.id.textArea)
            val ImageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            ContentTitle.text = item.title

            Glide.with(context)
                .load(item.imageUrl)
                .into(ImageViewArea)

        }
    }


}
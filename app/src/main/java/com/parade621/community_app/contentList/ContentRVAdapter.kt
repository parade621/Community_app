package com.parade621.community_app.contentList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.parade621.community_app.R
import com.parade621.community_app.utils.FBAuth
import com.parade621.community_app.utils.FBRef

class ContentRVAdapter(val context : Context,
                       val items: ArrayList<ContentModel>,
                       val itemKeyList:ArrayList<String>,
                       val bookmarIdkList:MutableList<String>?):RecyclerView.Adapter<ContentRVAdapter.ViewHolder>() {

    /*interface ItemClick{
        fun onClick(view : View, position: Int)
    }
    var itemClick : ItemClick? = null*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int):ContentRVAdapter.ViewHolder{

        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_items, parent,false)

        Log.d("IDValueCheckLog",itemKeyList.toString())
        Log.d("IDValueCheckLog",bookmarIdkList.toString())

        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ContentRVAdapter.ViewHolder, position:Int){
/*        if(itemClick !=null){
            holder.itemView.setOnClickListener { v->
                itemClick?.onClick(v, position)
            }
        }*/
        holder.bindItems(items[position], itemKeyList[position])
    }
    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindItems(item : ContentModel, key: String){

            // content 클릭 시, 해당 액티비티 실행.
            itemView.setOnClickListener {
                Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("url",item.url)
                itemView.context.startActivity(intent)
            }

            // content 구성성
            val ContentTitle = itemView.findViewById<TextView>(R.id.textArea)
            val ImageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            if(bookmarIdkList!!.contains(key)){
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            }else{
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }


            ContentTitle.text = item.title

            Glide.with(context)
                .load(item.imageUrl)
                .into(ImageViewArea)

            bookmarkArea.setOnClickListener {
                Log.d("ContentRVAdapter",FBAuth.getUid())

                if(bookmarIdkList!!.contains(key)) {
                    // 북마크 삭제
                    Toast.makeText(context, "북마크 삭제", Toast.LENGTH_SHORT).show()
                    FBRef.removeBookmark(key)
                }else {
                    // 북마크 추가
                    Toast.makeText(context, "북마크 추가", Toast.LENGTH_SHORT).show()
                    FBRef.addBookmark(key, item)
                }
            }

        }
    }


}
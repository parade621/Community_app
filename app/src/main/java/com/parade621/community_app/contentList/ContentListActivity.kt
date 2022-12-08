package com.parade621.community_app.contentList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.parade621.community_app.R

class ContentListActivity : AppCompatActivity() {

    lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val category = intent.getStringExtra("category")

        val database = Firebase.database
//        val myRef = database.getReference("contents")
//        val myRef2 = database.getReference("contents2")
        val items= ArrayList<ContentModel>()

        val rv : RecyclerView = findViewById(R.id.rv)
        val rvAdapter = ContentRVAdapter(baseContext,items)
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(this, 2)


        when (category){
            "category1"-> {myRef = database.getReference("contents")}
            "category2"-> {myRef = database.getReference("contents2")}
            else -> Toast.makeText(this,"미구현",Toast.LENGTH_SHORT).show()
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children){
                    Log.d("dataSnapShot Check", dataModel.toString())
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)

        //myRef.push().setValue("Hello, World!")



        rvAdapter.itemClick = object:ContentRVAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                Toast.makeText(baseContext,items[position].title,Toast.LENGTH_SHORT).show()

                val intent = Intent(this@ContentListActivity, ContentShowActivity::class.java)
                intent.putExtra("url",items[position].url)
                startActivity(intent)
            }

        }

    }
}
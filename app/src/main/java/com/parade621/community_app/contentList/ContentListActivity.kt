package com.parade621.community_app.contentList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.parade621.community_app.R
import com.parade621.community_app.databinding.ActivityContentListBinding
import com.parade621.community_app.utils.FBAuth
import com.parade621.community_app.utils.FBRef

class ContentListActivity : AppCompatActivity() {

    lateinit var myRef: DatabaseReference
    lateinit var binding: ActivityContentListBinding
    lateinit var rvAdapter: ContentRVAdapter
    val bookmarkList=mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_content_list)

        val category = intent.getStringExtra("category")

        val database = Firebase.database
        val items= ArrayList<ContentModel>()
        val itemKeyList = ArrayList<String>()

        val rv : RecyclerView = findViewById(R.id.rv)
        rvAdapter = ContentRVAdapter(baseContext,items, itemKeyList,bookmarkList)
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(this, 2)


        when (category){
            "category1"-> {myRef = database.getReference("contents")
                binding.categoryTitle.text = "전체보기"
            }
            "category2"-> {myRef = database.getReference("contents2")}
            else -> Toast.makeText(this,"미구현",Toast.LENGTH_SHORT).show()
        }

        // Content를 받아오는 부분.
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children){
                    //Log.d("ContentListActivity", dataModel.toString())
                    //Log.d("ContentListActivity", dataModel.key.toString())
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString()) // 이 key값을 Adapter로 넘겨서 bookmark 저장을 하도록 할 것.
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)

        getBookmarkData()

    }

    private fun getBookmarkData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //bookamrkList에 중첩해서 쌓이는 것을 방지하기 위함.
                bookmarkList.clear()

                for (dataModel in dataSnapshot.children){
                    bookmarkList.add(dataModel.key.toString())
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }
}
package com.parade621.community_app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.parade621.community_app.R
import com.parade621.community_app.contentList.ContentModel
import com.parade621.community_app.contentList.ContentRVAdapter
import com.parade621.community_app.databinding.FragmentBookmarkBinding
import com.parade621.community_app.utils.FBAuth
import com.parade621.community_app.utils.FBRef

class BookmarkFragment : Fragment() {

    private lateinit var binding : FragmentBookmarkBinding
    private lateinit var rvAdapter:ContentRVAdapter
    val bookmarkList=mutableListOf<String>()
    val items= ArrayList<ContentModel>()
    val itemKeyList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bookmark,container,false)

        // 기능 목록
        // 1. 전체 카테고리에 있는 컨텐츠 데이터들을 다 가져옴.
        // 2. 사용자가 북마크한 정보를 다 가져옴
        // 3. 전체 컨텐츠 중에서, 사용자가 북마크한 정보만 보여줌.

        // 2. 전체 카테고리에 있는 컨텐츠 데이터들을 다 가져옴


        val rv : RecyclerView = binding.bookmarkRV
        rvAdapter = ContentRVAdapter(binding.root.context ,items, itemKeyList,bookmarkList)
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(binding.root.context, 2)
        getBookmarkData()



        binding.homeTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment)

        }
        binding.goodtipTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)

        }
        binding.talkTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)

        }
        binding.bookmarkTab.setOnClickListener {

            Toast.makeText(context, "현재 탭입니다!", Toast.LENGTH_SHORT).show()

        }
        binding.storeTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)

        }


        return binding.root
    }

    private fun getBookmarkData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                bookmarkList.clear()
                items.clear()
                itemKeyList.clear()

                //bookamrkList에 중첩해서 쌓이는 것을 방지하기 위함.
                bookmarkList.clear()

                for (dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString()) // 이 key값을 Adapter로 넘겨서 bookmark 저장을 하도록 할 것.
                    bookmarkList.add(dataModel.key.toString())
                    Log.d("CheckThisData",bookmarkList.toString())
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
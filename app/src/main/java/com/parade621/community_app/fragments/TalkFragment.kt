package com.parade621.community_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.parade621.community_app.R
import com.parade621.community_app.board.BoardActivity
import com.parade621.community_app.board.BoardLVAdapter
import com.parade621.community_app.board.BoardModel
import com.parade621.community_app.board.BoardWriteActivity
import com.parade621.community_app.contentList.ContentModel
import com.parade621.community_app.databinding.FragmentTalkBinding
import com.parade621.community_app.utils.FBAuth
import com.parade621.community_app.utils.FBRef

class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding
    lateinit var lvAdapter: BoardLVAdapter
    val boardList = mutableListOf<BoardModel>()
    val boardKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_talk,container,false)


        lvAdapter = BoardLVAdapter(boardList)
        binding.boardListView.adapter = lvAdapter

        // 여기서 firebase에서 board 게시글들을 받아와서 리스트뷰에 넣어줄 거임.
        getBoardData()

        // 게시글 보는 액티비티를 만들거임.
        // 두가지 방법이 있는데,
        // 여기서 받은 정보를 putExtra의 형태로 넘겨주거나,
        // 나는 firebase의 key값을 전달해서 다시 데이터를 받아오는 방법을 사용하겠음.
        binding.boardListView.setOnItemClickListener { adapterView, view, position, id ->

            val intent = Intent(context,BoardActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)
        }


        // 우측 하단 wirte 버튼 클릭 시, 게시글 작성 액티비티 실행.
        binding.writeBtn.setOnClickListener {
            val intent = Intent(context,BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.homeTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)

        }
        binding.goodtipTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)

        }
        binding.talkTab.setOnClickListener {

            Toast.makeText(context, "현재 탭입니다!", Toast.LENGTH_SHORT).show()

        }
        binding.bookmarkTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)

        }
        binding.storeTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getBoardData()
    }

    fun getBoardData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardList.clear()
                boardKeyList.clear()

                for (dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                lvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }

}
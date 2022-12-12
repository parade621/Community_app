package com.parade621.community_app.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.parade621.community_app.R
import com.parade621.community_app.databinding.ActivityBoardBinding
import com.parade621.community_app.utils.FBAuth
import com.parade621.community_app.utils.FBRef

class BoardActivity : AppCompatActivity() {

    lateinit var boardData:BoardModel

    private lateinit var binding:ActivityBoardBinding
    private lateinit var storage:FirebaseStorage
    private lateinit var key:String
    private lateinit var uid:String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        /* 수정 예정 사항 게시판 */
        // 1. 수정 삭제 나중에 옵션메뉴 형식으로 바꿀거임.
        // 2. 댓글은 동적으로 생성되게 만들어 볼 예정임. <- 이것도 리스트 뷰를 이용하지 않을까 싶음.

        // 변수
        binding = DataBindingUtil.setContentView(this,R.layout.activity_board)
        storage = Firebase.storage

        // 액티비티 실행시 설정사항
        key= intent.getStringExtra("key").toString()

        /*// 주요 기능
        setBoard() // FB RB에서 게시글 받아오기
        setImage() // FB Storage에서 사진 받아오기
        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }*/

    }

    override fun onStart() {
        super.onStart()
        // 주요 기능
        setBoard() // FB RB에서 게시글 받아오기
        setImage() // FB Storage에서 사진 받아오기
        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }
    }

    private fun setBoard(){
        // firebase의 데이터베이스에서 key값에 해당하는 게시글을 받아옴.
        // 이후, 바로 액티비티의 뷰에 적용시킨다.

        // ***추가 Notice***
        // 밑에 Dialog에서 삭제하는 기능을 구현하면, 이 부분때문에 앱이 죽음. 그래서 try~catch문으로 작성함.
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    boardData = dataSnapshot.getValue(BoardModel::class.java)!!

                    binding.boardTimeArea.text = boardData.time
                    binding.boardTitleArea.text = boardData.title
                    binding.boardContentArea.text = boardData.content
                    uid = boardData.uid

                }catch(e:Exception){
                    Log.d("${key}_Board","삭제완료")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun setImage(){

        // 파이어베이스 공식문서 함수가 작동이 안되면 아래처럼 진행.

        Log.d("image_key_check",key)

        val storageReference = Firebase.storage.reference.child("${key}.png")
        val imageView = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageView)
            }else{

            }
        })
    }

    private fun showDialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<AppCompatButton>(R.id.modifyBtn)?.setOnClickListener {
            if(uid == FBAuth.getUid()) {
                val intent = Intent(baseContext, BoardEditActivity::class.java)
                intent.putExtra("key", key)
                startActivity(intent)
                alertDialog.cancel()
            }else {
                Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }

        }
        alertDialog.findViewById<AppCompatButton>(R.id.deleteBtn)?.setOnClickListener {
            if(uid == FBAuth.getUid()) {
                FBRef.boardRef.child(key).removeValue()
                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }else {
                Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
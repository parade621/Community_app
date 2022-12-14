package com.parade621.community_app.board

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
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
import com.parade621.community_app.utils.Time

class BoardActivity : AppCompatActivity() {

    lateinit var boardData:BoardModel

    private lateinit var binding:ActivityBoardBinding
    private lateinit var storage:FirebaseStorage
    private lateinit var key:String
    lateinit var clvAdapter:CommentLVAdapter
    val commentList = mutableListOf<CommentModel>()

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

        // 댓글 어뎁터 설정
        clvAdapter = CommentLVAdapter(commentList)
        binding.commentListView.adapter = clvAdapter


    }

    override fun onStart() {
        super.onStart()
        // 주요 기능
        setBoard() // FB RB에서 게시글 받아오기
        setImage() // FB Storage에서 사진 받아오기
        //setComment() // FB RB에서 게시글의 댓글 받아오기
        getComment()
    }

    override fun onResume() {
        super.onResume()
        binding.boardSettingIcon.setOnClickListener {
            showDialog()
        }
        binding.commentBtn.setOnClickListener {
            insertComment()
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

                    // 밑에 이걸 여기서 안해주면 계속 uid관련 오류가 남ㅠㅠ
                    if (FBAuth.getUid().equals(boardData.uid)){
                        // 글쓴이만 수정/삭제 가능.
                        binding.boardSettingIcon.isVisible = true
                    }


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

    private fun showDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.board_edit_custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<AppCompatButton>(R.id.modifyBtn)?.setOnClickListener {
            val intent = Intent(baseContext, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
            alertDialog.cancel()
        }
        alertDialog.findViewById<AppCompatButton>(R.id.deleteBtn)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            finish()

        }
    }

    fun insertComment(){
        val comment = binding.commentTextArea.text.toString()
        var items=CommentModel(comment,FBAuth.getUid(),Time.getTime())
        val commentKey = FBRef.addComment(key, items)
        /*boardData.commentList?.add(commentKey.toString())
        FBRef.editBoard(key,boardData)*/
        Toast.makeText(this,"댓글 등록 완료",Toast.LENGTH_SHORT).show()
        binding.commentTextArea.text.clear()
        binding.commentTextArea.clearFocus()
        onRestart()
    }

    fun getComment(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()
                var commentSize:Int = 0

                for (dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(CommentModel::class.java)
                    Log.d("왜 내용이 안나옴?",item?.comment.toString())
                    commentList.add(item!!)
                    commentSize++
                }
                setHeight(commentSize)
                clvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    fun setHeight(size:Int){
        val commentParam:ViewGroup.LayoutParams = binding.commentListView.layoutParams
        val displayMetrics = resources.displayMetrics
        val dp = Math.round((100*size+20) * displayMetrics.density) // 너무 딱 맞게 높이를 설정하면, ListView의 Scroll 기능이 나타나서 +20 해줌.
        commentParam.height = dp
        binding.commentListView.layoutParams=commentParam
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){
            val v = currentFocus
            if(v is EditText){
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if(!outRect.contains(event.rawX.toInt(),event.rawY.toInt())){
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken,0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}
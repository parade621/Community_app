package com.parade621.community_app.board

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
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
        // 게시물에 해당하는 이미지를 파이어베이스 저장소로 부터 받아온다.
        // 파이어베이스 공식문서 함수가 작동이 안되면 아래처럼 진행.

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
        // 게시글 수정과 삭제를 위한 다이얼로그 생성
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.board_edit_custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")
        val alertDialog = mBuilder.show() // 다이얼로그 표시

        alertDialog.findViewById<AppCompatButton>(R.id.modifyBtn)?.setOnClickListener {
            // 수정 버튼 클릭 시, 게시글을 수정하는 BoardEditActivity로 전환.
            // 이때 해당 게시글의 내용을 파이어베이스에서 받아오기 위해 게시글의 key값을 intent로 함께 넘겨준다.
            val intent = Intent(baseContext, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
            alertDialog.cancel()
        }
        alertDialog.findViewById<AppCompatButton>(R.id.deleteBtn)?.setOnClickListener {
            // 삭제 버튼 클릭 시, 게시글을 삭제하는 로직이다.
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun insertComment(){
        // 댓글 등록 시 작동되는 함수이다.
        // 게시글의 Key값에 저장된다.
        // 댓글 작성 후, 모든 댓글을 표시하기 위해 onRestart() 생명주기를 호출한다.
        val comment = binding.commentTextArea.text.toString()
        var items=CommentModel(comment,FBAuth.getUid(),Time.getTime())

        FBRef.addComment(key, items)

        Toast.makeText(this,"댓글 등록 완료",Toast.LENGTH_SHORT).show()

        binding.commentTextArea.text.clear()
        binding.commentTextArea.clearFocus()
        onRestart()
    }

    fun getComment(){
        // 게시글에 작성되어 있는 댓글을 가져와서 ListView료 표시하기 위한 로직이다.
        // 파이어베이스에서 게시물 key를 가진 댓글을 받아온다.
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()
                var commentSize:Int = 0 // 댓글창 크기 설정을 위해 댓글의 총 갯수를 받는다.

                for (dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentList.add(item!!)
                    commentSize++
                }
                // 차후에 코루틴과 kotlin Flow를 공부한 후, 댓글 창의 높이를 동적으로 받는 코드를 반드시 만들겠다.
                setHeight(commentSize) // 댓글창 크기를 설정하기 위한 함수
                clvAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }

    fun setHeight(size:Int){
        // 댓글창 설정을 위한 함수.
        // 하지만, 사이즈 계산을 위한 정수를 미리 설정해 둬서 글의 길이가 일정 이상 넘어가면 리스트 뷰의 스크롤이 생긴다.
        // 이를 해결하기 위해서는 코루틴을 사용해야 할 것이라 생각된다.
        // 코루틴 공부 후, 해당 기능을 완성하겠다.
        val commentParam:ViewGroup.LayoutParams = binding.commentListView.layoutParams
        val displayMetrics = resources.displayMetrics
        val dp = Math.round((200*size+20) * displayMetrics.density) // 너무 딱 맞게 높이를 설정하면, ListView의 Scroll 기능이 나타나서 +20 해줌.
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
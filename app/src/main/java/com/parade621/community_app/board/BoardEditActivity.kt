package com.parade621.community_app.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
import com.parade621.community_app.databinding.ActivityBoardEditBinding
import com.parade621.community_app.utils.FBAuth
import com.parade621.community_app.utils.FBRef
import com.parade621.community_app.utils.Time
import java.io.ByteArrayOutputStream

class BoardEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardEditBinding
    private lateinit var key:String
    private lateinit var storage:FirebaseStorage
    private lateinit var originalTime:String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)
        key = intent.getStringExtra("key").toString()

        storage = Firebase.storage

        getBoardData()
        getImageData()

        // 이미지를 내부 저장소(엘범)에서 불러오는 코드
        binding.imageUploadBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,100)

        }

        binding.uploadBtn.setOnClickListener{

            val title = binding.boardTitleArea.text.toString()
            val content = binding.boardContentArea.text.toString()

            var items:BoardModel?
            items= BoardModel(title,content, FBAuth.getUid(), originalTime)

            FBRef.editBoard(key, items) // 게시글의 key값을 받아온다.
            // 이미지 이름을 아무렇게나 넣으면, 이미지 이름에 대한 정보를 모르기 때문에, key값으로 줘서 찾기 쉽게 하는 거임.
            // 다수 이미지를 추가하는 기능도 추가 예정.

            // 이미지가 변경되었을 때만 업로드
            if(binding.imageUploadBtn.tag != "Initialized_image") imageUploadToFB()

            Toast.makeText(this, "게시글 수정 완료", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun getBoardData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val boardData = dataSnapshot.getValue(BoardModel::class.java)!!

                    binding.boardTitleArea.setText(boardData.title)
                    binding.boardContentArea.setText(boardData.content)

                    // 괜히 이거 만들어보겠다고 설쳤다.
                    if(boardData.time.contains("수정됨")) {
                        originalTime=boardData.time
                        // 이부분 작성안해서 앱 터지는걸 한참 못찾음.
                    }else originalTime=boardData.time+" 수정됨"

                }catch(e:Exception){
                    Log.d("${key}_Board","맛감")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun getImageData() {

        // 파이어베이스 공식문서 함수가 작동이 안되면 아래처럼 진행.

        Log.d("image_key_check", key)

        val storageReference = Firebase.storage.reference.child("${key}.png")
        val imageView = binding.imageUploadBtn

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageView)
            } else {

            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.imageUploadBtn.setImageURI(data?.data)
            binding.imageUploadBtn.tag="changed" // If the image changed, then the tag will be changed too.
        }
    }

    private fun imageUploadToFB(){

        val storageRef = storage.reference
        val mountainsRef = storageRef.child("${key}.png")
        val imageView = binding.imageUploadBtn

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

}
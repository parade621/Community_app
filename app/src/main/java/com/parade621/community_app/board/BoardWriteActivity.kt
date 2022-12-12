package com.parade621.community_app.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.parade621.community_app.R
import com.parade621.community_app.databinding.ActivityBoardWriteBinding
import com.parade621.community_app.utils.FBAuth
import com.parade621.community_app.utils.FBRef
import com.parade621.community_app.utils.Time
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityBoardWriteBinding
    private lateinit var storage:FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_write)

        storage = Firebase.storage


        // 이미지를 내부 저장소(엘범)에서 불러오는 코드1
        binding.imageUploadBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,100)

        }


        binding.uploadBtn.setOnClickListener{

            val title = binding.boardTitleArea.text.toString()
            val content = binding.boardContentArea.text.toString()

            var items:BoardModel?
            items= BoardModel(title,content,FBAuth.getUid(),Time.getTime())

            val key = FBRef.addBoard(items) // 게시글의 key값을 받아온다.
            // 이미지 이름을 아무렇게나 넣으면, 이미지 이름에 대한 정보를 모르기 때문에, key값으로 줘서 찾기 쉽게 하는 거임.
            // 다수 이미지를 추가하는 기능도 추가 예정.



            //if(binding.imageUploadBtn.resources.to == "plusbtn" )
            // 이미지가 변경되었을 때만 업로드
            if(binding.imageUploadBtn.tag != "Initialized_image") imageUploadToFB(key)



            Toast.makeText(this, "게시글 입력 완료!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // 이미지를 내부 저장소(엘범)에서 불러오는 코드2
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.imageUploadBtn.setImageURI(data?.data)
            binding.imageUploadBtn.tag="changed" // If the image changed, then the tag will be changed too.
        }
    }

    private fun imageUploadToFB(key:String){

        // 게시글의 key값을 받아와서, 업로드된 이미지 이름으로 저장할거임.

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
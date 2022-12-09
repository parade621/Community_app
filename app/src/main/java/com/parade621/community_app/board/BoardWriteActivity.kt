package com.parade621.community_app.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.parade621.community_app.R
import com.parade621.community_app.databinding.ActivityBoardWriteBinding
import com.parade621.community_app.utils.FBAuth
import com.parade621.community_app.utils.FBRef
import com.parade621.community_app.utils.Time

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityBoardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_write)

        binding.uploadBtn.setOnClickListener{

            val title = binding.boardTitleArea.text.toString()
            val content = binding.boardContentArea.text.toString()

            var items:BoardModel?
            items= BoardModel(title,content,FBAuth.getUid(),Time.getTime())

            FBRef.addBoard(items)
            Toast.makeText(this, "게시글 입력 완료!", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}
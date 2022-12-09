package com.parade621.community_app.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.parade621.community_app.board.BoardModel
import com.parade621.community_app.contentList.ContentModel

// 싱글톤으로
class FBRef {

    companion object{

        val database = Firebase.database
        val bookmarkRef = database.getReference("bookmakr_list")
        val boardRef = database.getReference("board")
        val uid = FBAuth.getUid()

        // Adapter로부터 Key값과 DataModel을 받아옴.
        // 처음에는 통신 방식으로 만들었는데, 너무 비효율적이라 이미 보유한 데이터를 넘기는 방식으로 진행.
        fun addBookmark(key:String, item:ContentModel){
            bookmarkRef
                .child(FBAuth.getUid())
                .child(key)
                .setValue(item)
        }

        fun removeBookmark(key:String){
            bookmarkRef
                .child(FBAuth.getUid())
                .child(key)
                .removeValue()
        }

        fun addBoard(boardModle:BoardModel){
            // board
            //  -key
            //      -boardModel(title, content, 작성자 uid, upload time)
            //로 구현될것.
            boardRef
                .push()
                .setValue(boardModle)
        }

    }
}
package com.parade621.community_app.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.parade621.community_app.board.BoardModel
import com.parade621.community_app.board.CommentModel
import com.parade621.community_app.contentList.ContentModel

// 싱글톤으로
class FBRef {

    companion object{

        val database = Firebase.database
        val bookmarkRef = database.getReference("bookmakr_list") // 북마크 기능을 위한 레퍼런스
        val boardRef = database.getReference("board")   // 게시판을 위한 레퍼런스
        val commentRef = database.getReference("comment")
        val uid = FBAuth.getUid()

        // Adapter로부터 Key값과 DataModel을 받아옴.
        // 처음에는 통신 방식으로 만들었는데, 너무 비효율적이라 이미 보유한 데이터를 넘기는 방식으로 진행.
        fun addBookmark(key:String, item:ContentModel){
            // 북마크를 추가함.
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

        fun addBoard(boardModle:BoardModel):String{

            // board
            //  -key
            //      -boardModel(title, content, 작성자 uid, upload time)
            // FB storage에 사진을 넣기위해 key값을 반환.
            //로 구현될것.
            val key = boardRef.push().key.toString()
            boardRef
                .child(key)
                .setValue(boardModle)

            return key
        }
        fun editBoard(key:String,boardModle:BoardModel){
            //val key = boardRef.push().key.toString()
            boardRef
                .child(key)
                .setValue(boardModle)
        }

        fun addComment(key:String, commentModle:CommentModel){
            // 여기서 게시글에 comment key까지 넣어볼까?
            commentRef
                .child(key)
                .push()
                .setValue(commentModle)

        }

    }
}
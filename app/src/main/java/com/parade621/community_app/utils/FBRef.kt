package com.parade621.community_app.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.parade621.community_app.contentList.ContentModel

class FBRef {

    companion object{

        lateinit var myRef:DatabaseReference
        val database = Firebase.database

        fun getBookmarkRef():DatabaseReference{
            return database.getReference("bookmakr_list")
        }

        // Adapter로부터 Key값과 DataModel을 받아옴.
        // 처음에는 통신 방식으로 만들었는데, 너무 비효율적이라 이미 보유한 데이터를 넘기는 방식으로 진행.
        fun addBookmark(key:String, item:ContentModel){
            myRef = database.getReference("bookmakr_list")
            myRef.child(FBAuth.getUid()).child(key).setValue(item)
        }

        fun removeBookmark(key:String){
            myRef = getBookmarkRef()
            myRef.child(FBAuth.getUid()).child(key).removeValue()
        }

    }
}
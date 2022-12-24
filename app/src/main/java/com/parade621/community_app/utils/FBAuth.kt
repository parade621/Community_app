package com.parade621.community_app.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// 파이어베이스 유저인증을 위한 클래스이다.
// 전체 프로젝트에서 해당 기능은 하나만 있이면된다는 생각에 싱글톤 패턴으로 작성하였다.
class FBAuth {

    companion object{

        private lateinit var auth : FirebaseAuth

        fun getUid():String{

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }
        fun getUidEmailInfo():String{
            val user = Firebase.auth.currentUser
            val email = user?.email.toString()

            return email
        }
    }
}
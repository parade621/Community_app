package com.parade621.community_app.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// 싱글톤으로
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
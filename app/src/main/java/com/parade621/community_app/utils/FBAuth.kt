package com.parade621.community_app.utils

import com.google.firebase.auth.FirebaseAuth

// 싱글톤으로
class FBAuth {

    companion object{

        private lateinit var auth : FirebaseAuth

        fun getUid():String{

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }
    }
}
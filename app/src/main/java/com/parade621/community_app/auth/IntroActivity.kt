package com.parade621.community_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parade621.community_app.MainActivity
import com.parade621.community_app.R
import com.parade621.community_app.databinding.ActivityAuthIntroBinding
import com.parade621.community_app.databinding.ActivityIntroBinding


class IntroActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAuthIntroBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_intro)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth_intro)
        auth = Firebase.auth

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
        binding.unAuthJoinBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this,"환영합니다!", Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(this,"다시 시도해 주십시요", Toast.LENGTH_SHORT).show()

                    }
                }
            startActivity(intent)
        }

        // test git setting

    }
}
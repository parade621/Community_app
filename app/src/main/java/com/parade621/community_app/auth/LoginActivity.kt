package com.parade621.community_app.auth

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parade621.community_app.MainActivity
import com.parade621.community_app.R
import com.parade621.community_app.databinding.ActivityAuthLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthLoginBinding
    private lateinit var imm : InputMethodManager
    private lateinit var auth: FirebaseAuth
    private lateinit var ani : Animation

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_login)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_auth_login)
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        auth = Firebase.auth

        ani = AnimationUtils.loadAnimation(this, R.anim.shake)

        binding.loginBtn.setOnClickListener {

            imm.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )

            val email = binding.emailArea.text.toString()
            val password = binding.pwArea.text.toString()

            var LoginAccept:Boolean = true

            if (email.isEmpty()){
                LoginAccept=false
                Toast.makeText(this,"이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.emailArea.startAnimation(ani)
                binding.emailArea.requestFocus()
                imm.showSoftInput(binding.emailArea, InputMethodManager.SHOW_FORCED)

            }else if(password.isEmpty()){
                LoginAccept=false
                Toast.makeText(this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.emailArea.startAnimation(ani)
                binding.pwArea.requestFocus()
                imm.showSoftInput(binding.pwArea, InputMethodManager.SHOW_FORCED)
            }
            if (LoginAccept) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext,"환영합니다", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(baseContext,"로그인 실패", Toast.LENGTH_SHORT).show()
                            binding.pwArea.text.clear()
                            binding.pwArea.requestFocus()
                            imm.showSoftInput(binding.pwArea, InputMethodManager.SHOW_FORCED)
                        }
                    }
            }
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){
            val v = currentFocus
            if(v is EditText){
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if(!outRect.contains(event.rawX.toInt(),event.rawY.toInt())){
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken,0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}
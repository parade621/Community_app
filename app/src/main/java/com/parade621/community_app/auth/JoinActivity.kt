package com.parade621.community_app.auth

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.parade621.community_app.databinding.ActivityAuthJoinBinding


class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAuthJoinBinding
    private lateinit var inputMethodManager : InputMethodManager
    private lateinit var animation:Animation

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_join)
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth_join)
        animation = AnimationUtils.loadAnimation(this, R.anim.shake)

        binding.joinBtn.setOnClickListener {

            inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )

            var joinAccept:Boolean = true

            val email = binding.emailTextArea.text.toString()
            val password = binding.pwTextArea.text.toString()

            // 값이 비어있는지 확인
            if(email.isEmpty()){
                joinAccept = false
                Toast.makeText(baseContext,"이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                binding.emailTextArea.requestFocus()
                inputMethodManager.showSoftInput(binding.emailTextArea, InputMethodManager.SHOW_FORCED)
            }
            else if(password.isEmpty()){
                joinAccept = false
                Toast.makeText(baseContext,"비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                binding.pwTextArea.requestFocus()
                inputMethodManager.showSoftInput(binding.pwTextArea, InputMethodManager.SHOW_FORCED)
            }
            else if(binding.pwChckTextArea.toString().isEmpty()){
                joinAccept = false
                Toast.makeText(baseContext,"비밀번호 확인란을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                binding.pwChckTextArea.requestFocus()
                inputMethodManager.showSoftInput(binding.pwChckTextArea, InputMethodManager.SHOW_FORCED)
            }
            else if (password != binding.pwChckTextArea.text.toString()) {
                joinAccept = false
                binding.pwChckTextArea.text.clear()
                Toast.makeText(baseContext, "설정한 비밀번호와 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                binding.pwChckTextArea.requestFocus()
                inputMethodManager.showSoftInput(
                    binding.pwChckTextArea,
                    InputMethodManager.SHOW_FORCED
                )
            }
            else if(password.length < 6){
                joinAccept=false
                Toast.makeText(this,"비밀번호를 6자리 이상으로 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.pwTextArea.text.clear()
                binding.pwChckTextArea.text.clear()
                binding.pwTextArea.requestFocus()
                inputMethodManager.showSoftInput(binding.pwTextArea, InputMethodManager.SHOW_FORCED)
            }

            if(joinAccept == true){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else {
                            // 이메일 중복 시, 다시 요청하는 코드.
                            try{
                                task.getResult()
                            }catch (e: Exception) {
                                e.printStackTrace()
                                Log.d("EmailErrorLog",e.message.toString())
                                Toast.makeText(baseContext, "회원가입 실패: 이미 존재하는 이메일 양식입니다.", Toast.LENGTH_SHORT).show()
                                binding.emailTextArea.startAnimation(animation)
                                binding.emailTextArea.requestFocus()
                                inputMethodManager.showSoftInput(binding.emailTextArea, InputMethodManager.SHOW_FORCED)
                            }

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
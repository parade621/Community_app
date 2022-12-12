package com.parade621.community_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.parade621.community_app.auth.IntroActivity
import com.parade621.community_app.databinding.ActivityMainBinding
import com.parade621.community_app.fragments.HomeFragment
import com.parade621.community_app.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var homeFragment : HomeFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth


        //드로워로 대체될 예정.
        binding.sideMenuBtn.setOnClickListener {

            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }
    }
}
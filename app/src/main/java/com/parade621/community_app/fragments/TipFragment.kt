package com.parade621.community_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.parade621.community_app.R
import com.parade621.community_app.contentList.ContentListActivity
import com.parade621.community_app.databinding.FragmentTipBinding

class TipFragment : Fragment() {

    private lateinit var binding : FragmentTipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tip,container,false)

        binding.category1.setOnClickListener {
            try{
                val intent = Intent(context, ContentListActivity::class.java)
                intent.putExtra("category","category1")
                startActivity(intent)
            }catch(e:Exception){
                Log.e("THis Error occurred!", e.message.toString())
            }
        }
        binding.category2.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category","category2")
            startActivity(intent)
        }
        binding.category3.setOnClickListener {
            val intent = Intent(context, ContentListActivity::class.java)
            intent.putExtra("category","category3")
            startActivity(intent)
        }

        binding.homeTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_tipFragment_to_homeFragment)

        }
        binding.goodtipTab.setOnClickListener {

            Toast.makeText(context, "현재 탭입니다!", Toast.LENGTH_SHORT).show()

        }
        binding.talkTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_tipFragment_to_talkFragment)

        }
        binding.bookmarkTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_tipFragment_to_bookmarkFragment)

        }
        binding.storeTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_tipFragment_to_storeFragment)

        }

        return binding.root
    }

}
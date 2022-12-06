package com.parade621.community_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.parade621.community_app.R
import com.parade621.community_app.databinding.FragmentTalkBinding

class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_talk,container,false)

        binding.homeTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)

        }
        binding.goodtipTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)

        }
        binding.talkTab.setOnClickListener {

            Toast.makeText(context, "현재 탭입니다!", Toast.LENGTH_SHORT).show()

        }
        binding.bookmarkTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)

        }
        binding.storeTab.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)

        }

        return binding.root
    }

}
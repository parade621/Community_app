package com.parade621.community_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.parade621.community_app.R
import com.parade621.community_app.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {

    private lateinit var binding : FragmentStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_store, container, false)

        val webView : WebView = view.findViewById(R.id.storeWebView)
        webView.loadUrl("https://www.inflearn.com/")

        return view


//        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_store,container,false)
//
//        binding.homeTab.setOnClickListener {
//
//            it.findNavController().navigate(R.id.action_storeFragment_to_homeFragment)
//
//        }
//        binding.goodtipTab.setOnClickListener {
//
//            it.findNavController().navigate(R.id.action_storeFragment_to_tipFragment)
//
//        }
//        binding.talkTab.setOnClickListener {
//
//            it.findNavController().navigate(R.id.action_storeFragment_to_talkFragment)
//
//        }
//        binding.bookmarkTab.setOnClickListener {
//
//            it.findNavController().navigate(R.id.action_storeFragment_to_bookmarkFragment)
//
//        }
//        binding.storeTab.setOnClickListener {
//
//            Toast.makeText(context, "현재 탭입니다!", Toast.LENGTH_SHORT).show()
//
//        }
//
//        return binding.root
    }


}
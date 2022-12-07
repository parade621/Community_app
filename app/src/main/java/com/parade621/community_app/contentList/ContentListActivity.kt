package com.parade621.community_app.contentList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parade621.community_app.R

class ContentListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val rv : RecyclerView = findViewById(R.id.rv)

        val items= ArrayList<String>()
        items.add("A")
        items.add("B")
        items.add("C")

        val rvAdapter = ContentRVAdapter(items)
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(this, 2)

    }
}
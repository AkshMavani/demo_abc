package com.example.filte


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.demo_full.R
import com.zanyastudios.test.PostItem
import com.zanyastudios.test.PostsAdapter
import java.util.Arrays


class MainActivity19 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main19)

        val postsRecyclerView: RecyclerView = findViewById(R.id.postsRecyclerView)
        postsRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val postItems: MutableList<PostItem> = mutableListOf()
        postItems.add(PostItem(R.drawable.leeminho))
        postItems.add(PostItem(R.drawable.leejongsuk))
        postItems.add(PostItem(R.drawable.chaeunwoo))
        postItems.add(PostItem(R.drawable.seokangjoon))
        postItems.add(PostItem(R.drawable.kimsoohyun))
        postItems.add(PostItem(R.drawable.parkseojoon))
        postItems.add(PostItem(R.drawable.seoinguk))
        postItems.add(PostItem(R.drawable.jichangwook))
        postItems.add(PostItem(R.drawable.yooseungho))
        postItems.add(PostItem(R.drawable.leeseunggi))

        postsRecyclerView.adapter = PostsAdapter(this, postItems)
    }
}
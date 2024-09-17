package com.blurview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R
import com.example.gallery.util.StickyHeaderGridLayoutManager

class MainActivity16 : AppCompatActivity() {
    private val SPAN_SIZE = 3
    private val SECTIONS = 10
    private val SECTION_ITEMS = 5

    lateinit var mRecycler: RecyclerView
    lateinit var mLayoutManager: StickyHeaderGridLayoutManager
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main16)

        // Setup recycler
        mRecycler = findViewById(R.id.recycler) as RecyclerView
        mLayoutManager =
            StickyHeaderGridLayoutManager(SPAN_SIZE)
     //   mLayoutManager.setHeaderBottomOverlapMargin(resources.getDimensionPixelSize(R.dimen))
        mLayoutManager.setSpanSizeLookup(object : StickyHeaderGridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(section: Int, position: Int): Int {
                Log.e("seciton", "section:$section ", )
                Log.e("seciton", "positon>>>>>>>>>:$position ", )
                return when (section) {
                    0 -> 3
                    1 -> 1
                    2 -> 3 - position % 3
                    3 -> position % 2 + 1
                    else -> 1
                }
            }
        })

        // Workaround RecyclerView limitation when playing remove animations. RecyclerView always
        // puts the removed item on the top of other views and it will be drawn above sticky header.
        // The only way to fix this, abandon remove animations :(

        // Workaround RecyclerView limitation when playing remove animations. RecyclerView always
        // puts the removed item on the top of other views and it will be drawn above sticky header.
        // The only way to fix this, abandon remove animations :(
        mRecycler.setItemAnimator(object : DefaultItemAnimator() {
            override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
                dispatchRemoveFinished(holder)
                return false
            }
        })
        mRecycler.layoutManager = mLayoutManager
        mRecycler.adapter =
            SampleAdapter(
                SECTIONS,
               SECTION_ITEMS
            )
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_top -> mRecycler.scrollToPosition(0)
            R.id.action_center -> mRecycler.scrollToPosition(mRecycler.adapter!!.itemCount / 2)
            R.id.action_bottom -> mRecycler.scrollToPosition(mRecycler.adapter!!.itemCount - 1)
            R.id.action_top_smooth -> mRecycler.smoothScrollToPosition(0)
            R.id.action_center_smooth -> mRecycler.smoothScrollToPosition(mRecycler.adapter!!.itemCount / 2)
            R.id.action_bottom_smooth -> mRecycler.smoothScrollToPosition(mRecycler.adapter!!.itemCount - 1)
        }
        return super.onOptionsItemSelected(item)
    }
}
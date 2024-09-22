package com.example.demo_full

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.gallery.FragmentMediaPager
import com.example.gallery.GalleryFragment
import com.google.android.material.tabs.TabLayout


class Activity_pager : AppCompatActivity() {

    lateinit var mTabs: TabLayout
    lateinit var mIndicator: View
    lateinit var mViewPager: ViewPager

    private var indicatorWidth = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)
        mTabs = findViewById<TabLayout>(R.id.tab)
        mIndicator = findViewById<View>(R.id.indicator)
        mViewPager = findViewById<ViewPager>(R.id.viewPager)

        //Set up the view pager and fragments

        //Set up the view pager and fragments
        val adapter = TabFragmentAdapter(supportFragmentManager)
        adapter.addFragment(FragmentMediaPager(), "All Images")
        adapter.addFragment(GalleryFragment(), "Gallry")
        mViewPager.setAdapter(adapter)
        mTabs.setupWithViewPager(mViewPager)

        //Determine indicator width at runtime

        //Determine indicator width at runtime
        mTabs.post(Runnable {
            indicatorWidth = mTabs.getWidth() / mTabs.getTabCount()

            //Assign new width
            val indicatorParams = mIndicator.getLayoutParams() as FrameLayout.LayoutParams
            indicatorParams.width = indicatorWidth
            mIndicator.setLayoutParams(indicatorParams)
        })

        mViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            override fun onPageScrolled(i: Int, positionOffset: Float, positionOffsetPx: Int) {
                val params = mIndicator.getLayoutParams() as FrameLayout.LayoutParams

                //Multiply positionOffset with indicatorWidth to get translation
                val translationOffset = (positionOffset + i) * indicatorWidth
                params.leftMargin = translationOffset.toInt()
                mIndicator.setLayoutParams(params)
            }

            override fun onPageSelected(i: Int) {}
            override fun onPageScrollStateChanged(i: Int) {}
        })
    }
}
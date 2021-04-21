package com.tutorial.foody.ui.activity.details.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(
    private val result: Bundle,
    private val fragments: ArrayList<Fragment>,
    private val titles: ArrayList<String>,
    fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment {
        fragments[position].arguments = result
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence = titles[position]
}
package com.tosh.poolandroid.view.appIntro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class IntroAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FirstFragment()
            1 -> return SecondFragment()
            2 -> return ThirdFragment()
            else -> return FirstFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }
}

package com.ashlikun.bottomnavigation.simple

import com.ashlikun.bottomnavigation.simple.DemoFragment.Companion.newInstance
import com.ashlikun.bottomnavigation.simple.DemoFragment
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

/**
 *
 */
class DemoViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    private val fragments = ArrayList<DemoFragment>()

    /**
     * Get the current fragment
     */
    var currentFragment: DemoFragment? = null
        private set

    init {
        fragments.clear()
        fragments.add(newInstance(0))
        fragments.add(newInstance(1))
        fragments.add(newInstance(2))
        fragments.add(newInstance(3))
        fragments.add(newInstance(4))
    }

    override fun getItem(position: Int): DemoFragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (currentFragment !== `object`) {
            currentFragment = `object` as DemoFragment
        }
        super.setPrimaryItem(container, position, `object`)
    }
}
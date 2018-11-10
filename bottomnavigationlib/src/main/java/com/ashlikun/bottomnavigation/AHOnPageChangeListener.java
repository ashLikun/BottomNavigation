package com.ashlikun.bottomnavigation;

import android.support.v4.view.ViewPager;

import java.lang.ref.WeakReference;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/11/10　14:10
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：结合viewPager使用的监听
 */
public class AHOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private final WeakReference<AHBottomNavigation> mNavigation;
    private int mPreviousScrollState;
    private int mScrollState;

    public AHOnPageChangeListener(AHBottomNavigation navigation) {
        mNavigation = new WeakReference<>(navigation);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mPreviousScrollState = mScrollState;
        mScrollState = state;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        final AHBottomNavigation navigation = mNavigation.get();
        if (navigation != null && navigation.getCurrentItem() != position) {
            if (navigation.tabSelectedListeners != null) {
                boolean selectionAllowed = true;
                for (AHBottomNavigation.OnTabSelectedListener l : navigation.tabSelectedListeners) {
                    if (!(l instanceof AHOnPageChangeListener)) {
                        if (!l.onTabSelected(position, false)) {
                            selectionAllowed = false;
                            break;
                        }
                    }
                }
                if (!selectionAllowed) {
                    return;
                }
            }
            navigation.setCurrentItem(position, false);
        }
    }
}

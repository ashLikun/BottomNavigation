/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ashlikun.bottomnavigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE;

/**
 * @author　　: 李坤
 * 创建时间: 2020/3/23 14:39
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：实现与ViewPager2 的联动
 */

public final class AHBottomNavigationMediator {
    @NonNull
    private final AHBottomNavigation bottomNavigation;
    @NonNull
    private final ViewPager2 viewPager;
    @Nullable
    private RecyclerView.Adapter<?> adapter;
    private boolean attached;

    @Nullable
    private AHBottomNavigationOnPageChangeCallback onPageChangeCallback;
    @Nullable
    private AHBottomNavigation.OnTabSelectedListener onTabSelectedListener;
    @Nullable
    private RecyclerView.AdapterDataObserver pagerAdapterObserver;

    /**
     * A callback interface that must be implemented to set the text and styling of newly created
     * tabs.
     */
    public interface NavConfigurationStrategy {
        /**
         * Called to configure the tab for the page at the specified position. Typically calls {@link
         * TabLayout.Tab#setText(CharSequence)}, but any form of styling can be applied.
         *
         * @param navigationItem The Tab which should be configured to represent the title of the item at the given
         *                       position in the data set.
         * @param position       The position of the item within the adapter's data set.
         */
        void onConfigureTab(@NonNull AHBottomNavigationItem.Builder navigationItem, int position);
    }

    public AHBottomNavigationMediator(
            @NonNull AHBottomNavigation bottomNavigation,
            @NonNull ViewPager2 viewPager) {
        this.bottomNavigation = bottomNavigation;
        this.viewPager = viewPager;
    }

    /**
     * Link the AHBottomNavigation  and the ViewPager2 together. Must be called after ViewPager2 has an adapter
     * set. To be called on a new instance of TabLayoutMediator or if the ViewPager2's adapter
     * changes.
     *
     * @throws IllegalStateException If the mediator is already attached, or the ViewPager2 has no
     *                               adapter.
     */
    public void attach() {
        if (attached) {
            throw new IllegalStateException("TabLayoutMediator is already attached");
        }
        adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException(
                    "TabLayoutMediator attached before ViewPager2 has an " + "adapter");
        }
        attached = true;

        // Add our custom OnPageChangeCallback to the ViewPager
        onPageChangeCallback = new AHBottomNavigationOnPageChangeCallback(bottomNavigation);
        viewPager.registerOnPageChangeCallback(onPageChangeCallback);

        // Now we'll add a tab selected listener to set ViewPager's current item
        onTabSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
        bottomNavigation.addOnTabSelectedListener(onTabSelectedListener);

        // Now we'll populate ourselves from the pager adapter, adding an observer if
        // autoRefresh is enabled
        if (autoRefresh) {
            // Register our observer on the new adapter
            pagerAdapterObserver = new PagerAdapterObserver();
            adapter.registerAdapterDataObserver(pagerAdapterObserver);
        }

        populateTabsFromPagerAdapter();

        // Now update the scroll position to match the ViewPager's current item
        bottomNavigation.setCurrentItem(viewPager.getCurrentItem(), false);
    }

    /**
     * Unlink the TabLayout and the ViewPager. To be called on a stale TabLayoutMediator if a new one
     * is instantiated, to prevent holding on to a view that should be garbage collected. Also to be
     * called before {@link #attach()} when a ViewPager2's adapter is changed.
     */
    public void detach() {
        if (autoRefresh && adapter != null) {
            adapter.unregisterAdapterDataObserver(pagerAdapterObserver);
            pagerAdapterObserver = null;
        }
        bottomNavigation.removeOnTabSelectedListener(onTabSelectedListener);
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
        onTabSelectedListener = null;
        onPageChangeCallback = null;
        adapter = null;
        attached = false;
    }

    @SuppressWarnings("WeakerAccess")
    void populateTabsFromPagerAdapter() {
        bottomNavigation.removeAllItems();

        if (adapter != null) {
            int adapterCount = adapter.getItemCount();
            for (int i = 0; i < adapterCount; i++) {
                AHBottomNavigationItem.Builder item = new AHBottomNavigationItem.Builder();
                configurationStrategy.onConfigureTab(item, i);
                bottomNavigation.addItem(item.builder());
            }
            // Make sure we reflect the currently set ViewPager item
            if (adapterCount > 0) {
                int lastItem = bottomNavigation.getItemsCount() - 1;
                int currItem = Math.min(viewPager.getCurrentItem(), lastItem);
                if (currItem != bottomNavigation.getCurrentItem()) {
                    bottomNavigation.setCurrentItem(currItem);
                }
            }
        }
    }

    /**
     * A {@link ViewPager2.OnPageChangeCallback} class which contains the necessary calls back to the
     * provided {@link TabLayout} so that the tab position is kept in sync.
     *
     * <p>This class stores the provided TabLayout weakly, meaning that you can use {@link
     * ViewPager2#registerOnPageChangeCallback(ViewPager2.OnPageChangeCallback)} without removing the
     * callback and not cause a leak.
     */
    private static class AHBottomNavigationOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        @NonNull
        private final WeakReference<AHBottomNavigation> navigationRef;
        private int previousScrollState;
        private int scrollState;

        AHBottomNavigationOnPageChangeCallback(AHBottomNavigation bottomNavigation) {
            navigationRef = new WeakReference<>(bottomNavigation);
            reset();
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            previousScrollState = scrollState;
            scrollState = state;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            final AHBottomNavigation navigation = navigationRef.get();
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

        void reset() {
            previousScrollState = scrollState = SCROLL_STATE_IDLE;
        }
    }

    /**
     * A {@link TabLayout.OnTabSelectedListener} class which contains the necessary calls back to the
     * provided {@link ViewPager2} so that the tab position is kept in sync.
     */
    private static class ViewPagerOnTabSelectedListener implements AHBottomNavigation.OnTabSelectedListener {
        private final ViewPager2 viewPager;

        ViewPagerOnTabSelectedListener(ViewPager2 viewPager) {
            this.viewPager = viewPager;
        }

        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            viewPager.setCurrentItem(position, true);
            return true;
        }
    }

    private class PagerAdapterObserver extends RecyclerView.AdapterDataObserver {
        PagerAdapterObserver() {
        }

        @Override
        public void onChanged() {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            populateTabsFromPagerAdapter();
        }
    }
}

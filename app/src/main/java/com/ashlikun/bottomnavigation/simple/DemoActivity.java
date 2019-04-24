package com.ashlikun.bottomnavigation.simple;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.ashlikun.bottomnavigation.AHBottomNavigation;
import com.ashlikun.bottomnavigation.AHBottomNavigationAdapter;
import com.ashlikun.bottomnavigation.AHBottomNavigationItem;
import com.ashlikun.bottomnavigation.notification.AHNotification;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class DemoActivity extends AppCompatActivity {

    private DemoFragment currentFragment;
    private DemoViewPagerAdapter adapter;
    private AHBottomNavigationAdapter navigationAdapter;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    private boolean useMenuResource = false;
    private int[] tabColors;
    private Handler handler = new Handler();

    // UI
    private ViewPager viewPager;
    private AHBottomNavigation bottomNavigation;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean enabledTranslucentNavigation = getSharedPreferences("shared", Context.MODE_PRIVATE)
                .getBoolean("translucentNavigation", false);
        setTheme(enabledTranslucentNavigation ? R.style.AppTheme_TranslucentNavigation : R.style.AppTheme);
        setContentView(R.layout.activity_home);
        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * Init UI
     */
    private void initUI() {

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);

//        if (useMenuResource) {
//            tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
//            navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
//            navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
//        } else {
//            AHBottomNavigationItem item1 = new AHBottomNavigationItem.Builder(R.string.tab_1, R.drawable.ic_apps_black_24dp)
//                    .setColorRes(R.color.color_tab_1).builder();
//            AHBottomNavigationItem item2 = new AHBottomNavigationItem.Builder(R.string.tab_2, R.drawable.ic_brightness_5_black_24dp)
//                    .setColorRes(R.color.color_tab_2).builder();
//            AHBottomNavigationItem item3 = new AHBottomNavigationItem.Builder(R.string.tab_3, R.drawable.ic_aspect_ratio_black_24dp)
//                    .setColorRes(R.color.color_tab_4).builder();
//            AHBottomNavigationItem item4 = new AHBottomNavigationItem.Builder(R.string.tab_3, R.drawable.ic_tab_4)
//                    .setColorRes(R.color.color_tab_4).builder();
//
//            bottomNavigationItems.add(item1);
//            bottomNavigationItems.add(item2);
//            bottomNavigationItems.add(item3);
//            bottomNavigationItems.add(item4);
//            bottomNavigation.addItems(bottomNavigationItems);
//        }
        bottomNavigation.setAccentColor(0xffffff00);
        bottomNavigation.setInactiveColor(0xffff0000);
        bottomNavigation.addItem(new AHBottomNavigationItem.Builder(R.string.main_bottom_1,
                R.mipmap.icon_home_no, R.mipmap.icon_home_select).builder());
        bottomNavigation.addItem(new AHBottomNavigationItem.Builder(R.string.main_bottom_2,
                R.mipmap.icon_find_gray, R.mipmap.icon_find_black).builder());
        bottomNavigation.addItem(new AHBottomNavigationItem.Builder(R.string.main_bottom_3,
                R.mipmap.icon_shejiao_gray, R.mipmap.icon_shejiao_black).builder());
        bottomNavigation.addItem(new AHBottomNavigationItem.Builder(R.string.main_bottom_4,
                R.mipmap.icon_my_gray, R.mipmap.icon_my_black)
                .builder());

        bottomNavigation.setDefaultBackgroundColor(0xffffffff);
        bottomNavigation.setCurrentItem(0, false);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);


        AHBottomNavigationItem item1 = new AHBottomNavigationItem.Builder(R.string.tab_1, R.drawable.ic_apps_black_24dp)
                .setColorRes(R.color.color_tab_1).builder();
        AHBottomNavigationItem item2 = new AHBottomNavigationItem.Builder(R.string.tab_2, R.drawable.ic_brightness_5_black_24dp)
                .setColorRes(R.color.color_tab_2).builder();
        AHBottomNavigationItem item3 = new AHBottomNavigationItem.Builder(R.string.tab_3, R.drawable.ic_aspect_ratio_black_24dp)
                .setColorRes(R.color.color_tab_3).builder();


        bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setForceTint(true);
        bottomNavigation.setupWithViewPager(viewPager);
        bottomNavigation.addOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (currentFragment == null) {
                    currentFragment = adapter.getCurrentFragment();
                }

                if (wasSelected) {
                    currentFragment.refresh();
                    return true;
                }

                if (currentFragment != null) {
                    currentFragment.willBeHidden();
                }

                viewPager.setCurrentItem(position, false);
                currentFragment = adapter.getCurrentFragment();
                currentFragment.willBeDisplayed();

                if (position == 1) {
                    bottomNavigation.setNotification("", 1);

                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setAlpha(0f);
                    floatingActionButton.setScaleX(0f);
                    floatingActionButton.setScaleY(0f);
                    floatingActionButton.animate()
                            .alpha(1)
                            .scaleX(1)
                            .scaleY(1)
                            .setDuration(300)
                            .setInterpolator(new OvershootInterpolator())
                            .setListener(null);
                }
                return true;
            }
        });


        adapter = new DemoViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        currentFragment = adapter.getCurrentFragment();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Setting custom colors for notification
                AHNotification notification = new AHNotification.Builder()
                        .setText(":)")
                        .setBackgroundColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_back))
                        .setTextColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_text))
                        .build();
                bottomNavigation.setNotification(notification, 1);
                Snackbar.make(bottomNavigation, "Snackbar with bottom navigation",
                        Snackbar.LENGTH_SHORT).show();

            }
        }, 3000);

        //bottomNavigation.setDefaultBackgroundResource(R.drawable.bottom_navigation_background);
    }

    /**
     * Update the bottom navigation colored param
     */
    public void updateBottomNavigationColor(boolean isColored) {
        bottomNavigation.setColored(isColored);
    }

    /**
     * Return if the bottom navigation is colored
     */
    public boolean isBottomNavigationColored() {
        return bottomNavigation.isColored();
    }

    /**
     * Add or remove items of the bottom navigation
     */
    public void updateBottomNavigationItems(boolean addItems) {

        if (useMenuResource) {
            if (addItems) {
                navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_5);
                navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
                bottomNavigation.setNotification("1", 3);
            } else {
                navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3);
                navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
            }

        } else {
            if (addItems) {
                AHBottomNavigationItem item4 = new AHBottomNavigationItem.Builder(getString(R.string.tab_4),
                        ContextCompat.getDrawable(this, R.drawable.ic_tab_4))
                        .setColor(ContextCompat.getColor(this, R.color.color_tab_4))
                        .builder();
                AHBottomNavigationItem item5 = new AHBottomNavigationItem.Builder(getString(R.string.tab_5),
                        ContextCompat.getDrawable(this, R.drawable.ic_tab_5))
                        .setColor(ContextCompat.getColor(this, R.color.color_tab_5))
                        .builder();

                bottomNavigation.addItem(item4);
                bottomNavigation.addItem(item5);
                bottomNavigation.setNotification("9", 3);
                bottomNavigation.setNotification(4);
            } else {
                bottomNavigation.removeAllItems();
                bottomNavigation.addItems(bottomNavigationItems);
            }
        }
    }

    /**
     * Show or hide the bottom navigation with animation
     */
    public void showOrHideBottomNavigation(boolean show) {
        if (show) {
            bottomNavigation.restoreBottomNavigation(true);
        } else {
            bottomNavigation.hideBottomNavigation(true);
        }
    }

    /**
     * Show or hide selected item background
     */
    public void updateSelectedBackgroundVisibility(boolean isVisible) {
        bottomNavigation.setSelectedBackgroundVisible(isVisible);
    }

    /**
     * Show or hide selected item background
     */
    public void setForceTitleHide(boolean forceTitleHide) {
        AHBottomNavigation.TitleState state = forceTitleHide ? AHBottomNavigation.TitleState.ALWAYS_HIDE : AHBottomNavigation.TitleState.ALWAYS_SHOW;
        bottomNavigation.setTitleState(state);
    }

    /**
     * Reload activity
     */
    public void reload() {
        startActivity(new Intent(this, DemoActivity.class));
        finish();
    }

    /**
     * Return the number of items in the bottom navigation
     */
    public int getBottomNavigationNbItems() {
        return bottomNavigation.getItemsCount();
    }

}

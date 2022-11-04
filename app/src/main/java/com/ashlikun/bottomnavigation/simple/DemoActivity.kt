package com.ashlikun.bottomnavigation.simple

import android.annotation.TargetApi
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import com.ashlikun.bottomnavigation.AHBottomNavigation
import com.ashlikun.bottomnavigation.AHBottomNavigation.TitleState
import com.ashlikun.bottomnavigation.AHBottomNavigationAdapter
import com.ashlikun.bottomnavigation.AHBottomNavigationItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DemoActivity : AppCompatActivity() {
    private var currentFragment: DemoFragment? = null
    private var adapter: DemoViewPagerAdapter? = null
    private var navigationAdapter: AHBottomNavigationAdapter? = null
    private val bottomNavigationItems = ArrayList<AHBottomNavigationItem>()
    private val useMenuResource = false
    private val tabColors: IntArray = intArrayOf()
    private val handler = Handler()
    private val statusBar by lazy {
        StatusBarCompat(this)
    }

    // UI
    private lateinit var viewPager: ViewPager
    private var bottomNavigation: AHBottomNavigation? = null
    private var floatingActionButton: FloatingActionButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        handleDisplayCutoutMode(window)
        statusBar.setStatusBarColor(0xffff0000.toInt())
        val enabledTranslucentNavigation = getSharedPreferences("shared", MODE_PRIVATE)
            .getBoolean("translucentNavigation", false)
        //        setTheme(enabledTranslucentNavigation ? R.style.AppTheme_TranslucentNavigation : R.style.AppTheme);
        setContentView(R.layout.activity_home)
        initUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * 刘海屏状态栏
     *
     * @param window
     */
    @TargetApi(28)
    private fun handleDisplayCutoutMode(window: Window) {
        val decorView = window.decorView
        if (decorView != null) {
            if (ViewCompat.isAttachedToWindow(decorView)) {
                realHandleDisplayCutoutMode(window, decorView)
            } else {
                decorView.addOnAttachStateChangeListener(object :
                    View.OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(v: View) {
                        v.removeOnAttachStateChangeListener(this)
                        realHandleDisplayCutoutMode(window, v)
                    }

                    override fun onViewDetachedFromWindow(v: View) {}
                })
            }
        }
    }

    /**
     * 刘海屏状态栏
     */
    @TargetApi(28)
    private fun realHandleDisplayCutoutMode(window: Window, decorView: View) {
        if (decorView.rootWindowInsets != null &&
            decorView.rootWindowInsets.displayCutout != null
        ) {
            val params = window.attributes
            //该窗口始终允许延伸到屏幕短边上的DisplayCutout区域
            //在非全屏模式下，这个方法（LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES）在部分10.0手机会导致水波纹卡顿，所以不用
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
    }

    /**
     * Init UI
     */
    private fun initUI() {
        bottomNavigation = findViewById<View>(R.id.bottom_navigation) as AHBottomNavigation
        viewPager = findViewById(R.id.view_pager)
        floatingActionButton = findViewById<View>(R.id.floating_action_button) as FloatingActionButton

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
        bottomNavigation!!.accentColor = -0x100
        bottomNavigation!!.inactiveColor = -0x10000
        bottomNavigation!!.addItem(
            AHBottomNavigationItem.Builder(
                R.string.main_bottom_1,
                R.mipmap.icon_home_no, R.mipmap.icon_home_select
            ).builder()
        )
        bottomNavigation!!.addItem(
            AHBottomNavigationItem.Builder(
                R.string.main_bottom_2,
                R.mipmap.icon_find_gray, R.mipmap.icon_find_black
            ).builder()
        )
        bottomNavigation!!.addItem(
            AHBottomNavigationItem.Builder(
                R.string.main_bottom_3,
                R.mipmap.icon_shejiao_gray, R.mipmap.icon_shejiao_black
            ).builder()
        )
        bottomNavigation!!.addItem(
            AHBottomNavigationItem.Builder(
                R.string.main_bottom_4,
                R.mipmap.icon_my_gray, R.mipmap.icon_my_black
            )
                .builder()
        )
        bottomNavigation!!.defaultBackgroundColor = -0xc0c0d
        bottomNavigation!!.setCurrentItem(0, false)
        bottomNavigation!!.titleState = TitleState.ALWAYS_SHOW
        val item1 = AHBottomNavigationItem.Builder(R.string.tab_1, R.drawable.ic_apps_black_24dp)
            .setColorRes(R.color.color_tab_1).builder()
        val item2 = AHBottomNavigationItem.Builder(R.string.tab_2, R.drawable.ic_brightness_5_black_24dp)
            .setColorRes(R.color.color_tab_2).builder()
        val item3 = AHBottomNavigationItem.Builder(R.string.tab_3, R.drawable.ic_aspect_ratio_black_24dp)
            .setColorRes(R.color.color_tab_3).builder()
        bottomNavigation!!.manageFloatingActionButtonBehavior(floatingActionButton)
        //        bottomNavigation.setTranslucentNavigationEnabled(true);
//        bottomNavigation.setForceTint(true);
        bottomNavigation!!.setupWithViewPager(viewPager, true)
        bottomNavigation!!.addOnTabSelectedListener(AHBottomNavigation.OnTabSelectedListener { position, wasSelected ->
            if (currentFragment == null) {
                currentFragment = adapter!!.currentFragment
            }
            if (wasSelected) {
                currentFragment!!.refresh()
                return@OnTabSelectedListener true
            }
            if (currentFragment != null) {
                currentFragment!!.willBeHidden()
            }
            viewPager.setCurrentItem(position, false)
            currentFragment = adapter!!.currentFragment
            currentFragment!!.willBeDisplayed()
            if (position == 1) {
                bottomNavigation!!.setNotification("", 1)
                floatingActionButton!!.visibility = View.VISIBLE
                floatingActionButton!!.alpha = 0f
                floatingActionButton!!.scaleX = 0f
                floatingActionButton!!.scaleY = 0f
                floatingActionButton!!.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300)
                    .setInterpolator(OvershootInterpolator())
                    .setListener(null)
            }
            true
        })
        adapter = DemoViewPagerAdapter(supportFragmentManager)
        viewPager.setAdapter(adapter)

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Setting custom colors for notification
//                AHNotification notification = new AHNotification.Builder()
//                        .setText(":)")
//                        .setBackgroundColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_back))
//                        .setTextColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_text))
//                        .build();
//                bottomNavigation.setNotification(notification, 1);
//                Snackbar.make(bottomNavigation, "Snackbar with bottom navigation",
//                        Snackbar.LENGTH_SHORT).show();
//
//            }
//        }, 3000);

        //bottomNavigation.setDefaultBackgroundResource(R.drawable.bottom_navigation_background);
    }

    /**
     * Update the bottom navigation colored param
     */
    fun updateBottomNavigationColor(isColored: Boolean) {
        bottomNavigation!!.isColored = isColored
    }

    /**
     * Return if the bottom navigation is colored
     */
    val isBottomNavigationColored: Boolean
        get() = bottomNavigation!!.isColored

    /**
     * Add or remove items of the bottom navigation
     */
    fun updateBottomNavigationItems(addItems: Boolean) {
        if (useMenuResource) {
            if (addItems) {
                navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_5)
                navigationAdapter!!.setupWithBottomNavigation(bottomNavigation, tabColors)
                bottomNavigation!!.setNotification("1", 3)
            } else {
                navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu_3)
                navigationAdapter!!.setupWithBottomNavigation(bottomNavigation, tabColors)
            }
        } else {
            if (addItems) {
                val item4 = AHBottomNavigationItem.Builder(
                    getString(R.string.tab_4),
                    ContextCompat.getDrawable(this, R.drawable.ic_tab_4)
                )
                    .setColor(ContextCompat.getColor(this, R.color.color_tab_4))
                    .builder()
                val item5 = AHBottomNavigationItem.Builder(
                    getString(R.string.tab_5),
                    ContextCompat.getDrawable(this, R.drawable.ic_tab_5)
                )
                    .setColor(ContextCompat.getColor(this, R.color.color_tab_5))
                    .builder()
                bottomNavigation!!.addItem(item4)
                bottomNavigation!!.addItem(item5)
                bottomNavigation!!.setNotification("9", 3)
                bottomNavigation!!.setNotification(4)
            } else {
                bottomNavigation!!.removeAllItems()
                bottomNavigation!!.addItems(bottomNavigationItems)
            }
        }
    }

    /**
     * Show or hide the bottom navigation with animation
     */
    fun showOrHideBottomNavigation(show: Boolean) {
        if (show) {
            bottomNavigation!!.restoreBottomNavigation(true)
        } else {
            bottomNavigation!!.hideBottomNavigation(true)
        }
    }

    /**
     * Show or hide selected item background
     */
    fun updateSelectedBackgroundVisibility(isVisible: Boolean) {
        bottomNavigation!!.setSelectedBackgroundVisible(isVisible)
    }

    /**
     * Show or hide selected item background
     */
    fun setForceTitleHide(forceTitleHide: Boolean) {
        val state = if (forceTitleHide) TitleState.ALWAYS_HIDE else TitleState.ALWAYS_SHOW
        bottomNavigation!!.titleState = state
    }

    /**
     * Reload activity
     */
    fun reload() {
        startActivity(Intent(this, DemoActivity::class.java))
        finish()
    }

    /**
     * Return the number of items in the bottom navigation
     */
    val bottomNavigationNbItems: Int
        get() = bottomNavigation!!.itemsCount
}
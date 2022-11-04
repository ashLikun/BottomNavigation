package com.ashlikun.bottomnavigation.simple

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 *
 */
class DemoFragment : Fragment() {
    private val fragmentContainer: LinearLayout? = null
    private val recyclerView: RecyclerView? = null
    private val layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (requireArguments().getInt("index", 0) == 0) {
            val view = inflater.inflate(R.layout.fragment_demo_settings, container, false)
            initDemoSettings(view)
            view
        } else {
            val view = inflater.inflate(R.layout.fragment_demo_list, container, false)
            initDemoList(view)
            view
        }
    }

    /**
     * Init demo settings
     */
    private fun initDemoSettings(view: View) {
        val demoActivity = activity as DemoActivity
        val switchColored = view.findViewById<View>(R.id.fragment_demo_switch_colored) as SwitchCompat
        val switchFiveItems = view.findViewById<View>(R.id.fragment_demo_switch_five_items) as SwitchCompat
        val showHideBottomNavigation = view.findViewById<View>(R.id.fragment_demo_show_hide) as SwitchCompat
        val showSelectedBackground = view.findViewById<View>(R.id.fragment_demo_selected_background) as SwitchCompat
        val switchForceTitleHide = view.findViewById<View>(R.id.fragment_demo_force_title_hide) as SwitchCompat
        val switchTranslucentNavigation = view.findViewById<View>(R.id.fragment_demo_translucent_navigation) as SwitchCompat
        switchColored.isChecked = demoActivity!!.isBottomNavigationColored
        switchFiveItems.isChecked = demoActivity.bottomNavigationNbItems == 5
        switchTranslucentNavigation.isChecked = demoActivity
            .getSharedPreferences("shared", Context.MODE_PRIVATE)
            .getBoolean("translucentNavigation", false)
        switchTranslucentNavigation.visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) View.VISIBLE else View.GONE
        switchTranslucentNavigation.setOnCheckedChangeListener { buttonView, isChecked ->
            demoActivity
                .getSharedPreferences("shared", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("translucentNavigation", isChecked)
                .apply()
            demoActivity.reload()
        }
        switchColored.setOnCheckedChangeListener { buttonView, isChecked -> demoActivity.updateBottomNavigationColor(isChecked) }
        switchFiveItems.setOnCheckedChangeListener { buttonView, isChecked -> demoActivity.updateBottomNavigationItems(isChecked) }
        showHideBottomNavigation.setOnCheckedChangeListener { buttonView, isChecked -> demoActivity.showOrHideBottomNavigation(isChecked) }
        showSelectedBackground.setOnCheckedChangeListener { buttonView, isChecked -> demoActivity.updateSelectedBackgroundVisibility(isChecked) }
        switchForceTitleHide.setOnCheckedChangeListener { buttonView, isChecked -> demoActivity.setForceTitleHide(isChecked) }
    }

    /**
     * Init the fragment
     */
    private fun initDemoList(view: View) {

//        fragmentContainer = view.findViewById(R.id.fragment_container);
//        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_demo_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//
//        ArrayList<String> itemsData = new ArrayList<>();
//        for (int i = 0; i < 50; i++) {
//            itemsData.add("Fragment " + getArguments().getInt("index", -1) + " / Item " + i);
//        }
//
//        DemoAdapter adapter = new DemoAdapter(itemsData);
//        recyclerView.setAdapter(adapter);
    }

    /**
     * Refresh
     */
    fun refresh() {
        if (arguments!!.getInt("index", 0) > 0 && recyclerView != null) {
            recyclerView.smoothScrollToPosition(0)
        }
    }

    /**
     * Called when a fragment will be displayed
     */
    fun willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            val fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
            fragmentContainer.startAnimation(fadeIn)
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    fun willBeHidden() {
        if (fragmentContainer != null) {
            val fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
            fragmentContainer.startAnimation(fadeOut)
        }
    }

    companion object {
        /**
         * Create a new instance of the fragment
         */
        @JvmStatic
        fun newInstance(index: Int): DemoFragment {
            val fragment = DemoFragment()
            val b = Bundle()
            b.putInt("index", index)
            fragment.arguments = b
            return fragment
        }
    }
}
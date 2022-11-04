package com.ashlikun.bottomnavigation.simple

import android.widget.TextView
import com.ashlikun.bottomnavigation.simple.R
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

/**
 *
 */
class DemoAdapter(dataset: ArrayList<String>?) : RecyclerView.Adapter<DemoAdapter.ViewHolder>() {
    private val mDataset = ArrayList<String>()

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTextView: TextView

        init {
            mTextView = v.findViewById<View>(R.id.layout_item_demo_title) as TextView
        }
    }

    init {
        mDataset.clear()
        mDataset.addAll(dataset!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_demo, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.text = mDataset[position]
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }
}
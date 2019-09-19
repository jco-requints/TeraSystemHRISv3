package com.example.terasystemhrisv3

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.terasystemhrisv3.model.Leaves
import kotlinx.android.synthetic.main.leaves_recyclerview_item_row.view.*

class LeavesRecyclerAdapter(private val leaves: ArrayList<Leaves>) : RecyclerView.Adapter<LeavesRecyclerAdapter.LeavesHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeavesHolder {
        val inflatedView = parent.inflate(R.layout.leaves_recyclerview_item_row, false)
        return LeavesHolder(inflatedView)
    }

    override fun getItemCount() = leaves.size

    override fun onBindViewHolder(holder: LeavesHolder, position: Int) {
        val itemLeaves = leaves[position]
        holder.bindLeaves(itemLeaves)
    }

    class LeavesHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var leaves: Leaves? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {

        }

        companion object {
            private val LEAVES_KEY = "LEAVES"
        }

        fun bindLeaves(leaves: Leaves) {
            this.leaves = leaves
            val dateFrom: String
            val dateTo: String
            if(leaves.dateTo.isNullOrEmpty() || leaves.dateTo == "null")
            {
                dateFrom = leaves.dateFrom
                view.leaveDuration.text = dateFrom
                view.leaveType.text = leaves.type
                view.numberOfDays.text = leaves.time
            }
            else
            {
                dateFrom = leaves.dateFrom
                dateTo = leaves.dateTo
                view.leaveDuration.text = "$dateFrom to $dateTo"
                view.leaveType.text = leaves.type
                view.numberOfDays.text = leaves.time
            }

        }

    }

}
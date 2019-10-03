package com.example.terasystemhrisv3.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.terasystemhrisv3.model.Leaves
import kotlinx.android.synthetic.main.leaves_recyclerview_item_row.view.*

class LeavesListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var view: View = itemView
    private var leaves: Leaves? = null

    companion object {
        private val LEAVES_KEY = "LEAVES"
    }

    fun bindLeaves(leaves: Leaves) {
        this.leaves = leaves
        val dateFrom: String
        val dateTo: String
        if(leaves.dateTo.isNullOrEmpty())
        {
            dateFrom = leaves.dateFrom!!
            view.leaveDuration.text = dateFrom
            view.leaveType.text = leaves.type
            view.numberOfDays.text = leaves.time
        }
        else if(leaves.dateFrom == leaves.dateTo)
        {
            dateFrom = leaves.dateFrom!!
            view.leaveDuration.text = dateFrom
            view.leaveType.text = leaves.type
            view.numberOfDays.text = leaves.time
        }
        else
        {
            dateFrom = leaves.dateFrom!!
            dateTo = leaves.dateTo!!
            view.leaveDuration.text = "$dateFrom to $dateTo"
            view.leaveType.text = leaves.type
            view.numberOfDays.text = leaves.time
        }

    }

}
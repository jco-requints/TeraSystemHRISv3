package com.example.terasystemhrisv3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.terasystemhrisv3.ui.HeaderViewHolder
import com.example.terasystemhrisv3.ui.LeavesListViewHolder
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.Leaves

class LeavesRecyclerAdapter(private var leavesList: ArrayList<Leaves>, private var remSL: Double, private var remVL: Double, private var showRemSLAndRemVL: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
                CellType.HEADER.ordinal -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.leaves_recyclerview_header, parent, false))
                CellType.CONTENT.ordinal -> LeavesListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.leaves_recyclerview_item_row, parent, false))
                else -> LeavesListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.leaves_recyclerview_item_row, parent, false))
        }
    }

    override fun getItemCount() = leavesList.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CellType.HEADER.ordinal -> {
                val headerViewHolder = holder as HeaderViewHolder
                headerViewHolder.bindLeaves(remSL, remVL, showRemSLAndRemVL)
            }
            CellType.CONTENT.ordinal -> {
                val headerViewHolder = holder as LeavesListViewHolder
                headerViewHolder.bindLeaves(leavesList[position - 1])
            }
        }
    }

    /***
     * This method will return cell type base on position
     */
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> CellType.HEADER.ordinal
            else -> CellType.CONTENT.ordinal
        }
    }

    /***
     * Enum class for recyclerview Cell type
     */
    enum class CellType(viewType: Int) {
        HEADER(0),
        CONTENT(1)
    }

    fun setLeavesList(leavesList: ArrayList<Leaves>) {
        this.leavesList = leavesList
        notifyDataSetChanged()
    }

}
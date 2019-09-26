package com.example.terasystemhrisv3.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.terasystemhrisv3.model.Leaves
import kotlinx.android.synthetic.main.leaves_recyclerview_header.view.*
import kotlinx.android.synthetic.main.leaves_recyclerview_item_row.view.*

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var view: View = itemView
    private var remSL: Double? = null
    private var remVL: Double? = null

    fun bindLeaves(remSL: Double, remVL: Double) {
        this.remSL = remSL
        this.remVL = remVL
        view.sickLeave.text = remSL.toString()
        view.vacationLeave.text = remVL.toString()

    }
}
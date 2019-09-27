package com.example.terasystemhrisv3.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.leaves_recyclerview_header.view.*

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var view: View = itemView
    private var remSL: Double? = null
    private var remVL: Double? = null

    fun bindLeaves(remSL: Double, remVL: Double, showRemSLAndRemVL: Boolean) {
        this.remSL = remSL
        this.remVL = remVL
        if(showRemSLAndRemVL)
        {
            view.sickLeave.text = remSL.toString()
            view.vacationLeave.text = remVL.toString()
        }
        else
        {
            view.sickLeave.text = ""
            view.vacationLeave.text = ""
        }
    }
}
package com.example.terasystemhrisv3.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.terasystemhrisv3.model.Logs
import kotlinx.android.synthetic.main.fragment_logs.view.itemTimeOut
import kotlinx.android.synthetic.main.leaves_recyclerview_item_row.view.leaveDuration
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*

class LogsViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

    private var view: View = v
    private var logs: Logs? = null

    init {
        v.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val item = logs!!
        val mBundle = Bundle()
        mBundle.putParcelable("item_selected_key", item)
        val context = itemView.context ?: return
        if (context is FragmentActivity) {
            context.showLogDetails(mBundle, LogDetailsFragment())
        }
    }

    companion object {
        private val LOGS_KEY = "LOGS"
    }

    fun bindLogs(logs: Logs) {
        this.logs = logs
        view.leaveDuration.text = logs.date
        if(logs.timeIn.isNullOrEmpty() ||logs.timeIn == "null")
        {
            view.itemTimeIn.text = "N/A"
            view.itemTimeIn.setTextColor(Color.RED)
        }
        else
        {
            view.itemTimeIn.text = logs.timeIn
            view.itemTimeIn.setTextColor(Color.BLACK)
        }
        if(logs.timeOut.isNullOrEmpty() || logs.timeOut == "null")
        {
            view.itemTimeOut.text = "N/A"
            view.itemTimeOut.setTextColor(Color.RED)
        }
        else
        {
            view.itemTimeOut.text = logs.timeOut
            view.itemTimeOut.setTextColor(Color.BLACK)
        }
    }

}
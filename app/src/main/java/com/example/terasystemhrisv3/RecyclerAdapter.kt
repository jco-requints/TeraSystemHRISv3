package com.example.terasystemhrisv3

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.terasystemhrisv3.model.Logs
import com.example.terasystemhrisv3.ui.FragmentActivity
import com.example.terasystemhrisv3.ui.LogDetailsFragment
import kotlinx.android.synthetic.main.fragment_logs.view.itemTimeOut
import kotlinx.android.synthetic.main.fragment_logs.view.leaveDuration
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*


class RecyclerAdapter(private val logs: ArrayList<Logs>) : RecyclerView.Adapter<RecyclerAdapter.LogsHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_item_row, false)
        return LogsHolder(inflatedView)
    }

    override fun getItemCount() = logs.size

    override fun onBindViewHolder(holder: LogsHolder, position: Int) {
        val itemLogs = logs[position]
        holder.bindLogs(itemLogs)
    }

    class LogsHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var logs: Logs? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val item = logs!!
            fragmentJump(item)
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
                view.itemTimeIn.setTextColor(Color.parseColor("#FF0000"))
            }
            else
            {
                view.itemTimeIn.text = logs.timeIn
                view.itemTimeIn.setTextColor(Color.parseColor("#FF000000"))
            }
            if(logs.timeOut.isNullOrEmpty() || logs.timeOut == "null")
            {
                view.itemTimeOut.text = "N/A"
                view.itemTimeOut.setTextColor(Color.parseColor("#FF0000"))
            }
            else
            {
                view.itemTimeOut.text = logs.timeOut
                view.itemTimeOut.setTextColor(Color.parseColor("#FF000000"))
            }
        }

        fun fragmentJump(logs: Logs){
            val mBundle = Bundle()
            mBundle.putParcelable("item_selected_key", logs)
            val fragment = LogDetailsFragment()
            fragment.arguments = mBundle
            switchContent(R.id.container, fragment)
        }

        private fun switchContent(id: Int, fragment: Fragment) {
            val context = itemView.context ?: return
            if (context is FragmentActivity) {
                context.switchContent(id, fragment)
            }

        }

    }

}
package com.example.terasystemhrisv3.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.terasystemhrisv3.AppBarController
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.Logs
import kotlinx.android.synthetic.main.fragment_logdetails.view.*

class LogDetailsFragment : Fragment() {

    private var myInterface: AppBarController? = null
    private var logDetails: Logs = Logs("","","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        if (bundle != null)
        {
            logDetails = bundle.getParcelable("item_selected_key")!!
        }
        val view = inflater.inflate(R.layout.fragment_logdetails, container, false)
        myInterface?.setTitle(logDetails.date)
        myInterface?.setAddButtonTitle(null)
        myInterface?.setCancelButtonTitle("<")
        myInterface?.getAddButton()?.visibility = View.GONE
        myInterface?.getCancelButton()?.visibility = View.VISIBLE
        myInterface?.getCancelButton()?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36F)
        view.timeIn?.text = logDetails.timeIn
        view.timeOut?.text = logDetails.timeOut
        view.breakIn?.text = logDetails.breakIn
        view.breakOut?.text = logDetails.breakOut
        myInterface?.getCancelButton()?.setOnClickListener {
            val fragmentManager = myInterface?.getSupportFragmentManager()
            fragmentManager?.popBackStackImmediate()
        }

        if(logDetails.timeIn.isNullOrEmpty() || logDetails.timeIn == "null")
        {
            view.timeIn?.text = "N/A"
            view.timeIn?.setTextColor(Color.parseColor("#FF0000"))
        }
        if(logDetails.timeOut.isNullOrEmpty() || logDetails.timeOut == "null")
        {
            view.timeOut?.text = "N/A"
            view.timeOut?.setTextColor(Color.parseColor("#FF0000"))
        }
        if(logDetails.breakIn.isNullOrEmpty() || logDetails.breakIn == "null")
        {
            view.breakIn?.text = "N/A"
            view.breakIn?.setTextColor(Color.parseColor("#FF0000"))
        }
        if(logDetails.breakOut.isNullOrEmpty() || logDetails.breakOut == "null")
        {
            view.breakOut?.text = "N/A"
            view.breakOut?.setTextColor(Color.parseColor("#FF0000"))
        }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context is AppBarController)
        {
            myInterface = context
        }
    }

    companion object {
        val TAG: String = LogDetailsFragment::class.java.simpleName
        fun newInstance(bundle: Bundle) = LogDetailsFragment().apply {
            this.arguments = bundle
        }
    }

}
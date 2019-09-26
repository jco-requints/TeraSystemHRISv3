package com.example.terasystemhrisv3.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.terasystemhrisv3.interfaces.AppBarController
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.Logs
import com.example.terasystemhrisv3.viewmodel.LogDetailsViewModel
import kotlinx.android.synthetic.main.fragment_logdetails.view.*

class LogDetailsFragment : Fragment() {

    private var myInterface: AppBarController? = null
    private var logDetails: Logs = Logs("","","","","","")
    private lateinit var logDetailsViewModel: LogDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        if (bundle != null)
        {
            logDetails = bundle.getParcelable("item_selected_key")!!
        }
        logDetailsViewModel = ViewModelProviders.of(this).get(LogDetailsViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_logdetails, container, false)
        myInterface?.setTitle(logDetails.date)
        myInterface?.setAddButtonTitle(null)
        myInterface?.setCancelButtonTitle("<")
        myInterface?.getAddButton()?.visibility = View.GONE
        myInterface?.getCancelButton()?.visibility = View.VISIBLE
        myInterface?.getCancelButton()?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36F)
        myInterface?.getCancelButton()?.setOnClickListener {
            val fragmentManager = myInterface?.getSupportFragmentManager()
            fragmentManager?.popBackStackImmediate()
        }
        view.timeIn?.text = logDetails.timeIn
        view.timeOut?.text = logDetails.timeOut
        view.breakIn?.text = logDetails.breakIn
        view.breakOut?.text = logDetails.breakOut

        logDetailsViewModel.loadLogDetails(logDetails.timeIn, logDetails.timeOut, logDetails.breakIn, logDetails.breakOut)

        logDetailsViewModel.isTimeInEmpty.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                view.timeIn?.text = "N/A"
                view.timeIn?.setTextColor(Color.RED)
            }
        })

        logDetailsViewModel.isTimeOutEmpty.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                view.timeOut?.text = "N/A"
                view.timeOut?.setTextColor(Color.RED)
            }
        })

        logDetailsViewModel.isBreakInEmpty.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                view.breakIn?.text = "N/A"
                view.breakIn?.setTextColor(Color.RED)
            }
        })

        logDetailsViewModel.isBreakOutEmpty.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                view.breakOut?.text = "N/A"
                view.breakOut?.setTextColor(Color.RED)
            }
        })

        return view
    }

    override fun onAttach(context: Context) {
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
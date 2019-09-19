package com.example.terasystemhrisv3.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.terasystemhrisv3.AppBarController
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails
import kotlinx.android.synthetic.main.fragment_addtimelogsuccess.view.*

class AddTimeLogSuccessFragment : Fragment() {

    private var myInterface: AppBarController? = null
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        var logType = 0
        var currentTime = ""
        if (bundle != null)
        {
            logType = bundle.getInt("logType")
            currentTime = bundle.getString("currentTime")!!
            myDetails = bundle.getParcelable("keyAccountDetails")!!
        }
        val view = inflater.inflate(R.layout.fragment_addtimelogsuccess, container, false)
        myInterface?.setTitle(getString(R.string.addtimelogsuccess_title))
        myInterface?.setAddButtonTitle(null)
        myInterface?.setCancelButtonTitle(null)
        myInterface?.getAddButton()?.visibility = View.GONE
        myInterface?.getCancelButton()?.visibility = View.GONE
        when (logType) {
            1 -> view.leaveType.text = getString(R.string.time_in_text)
            2 -> view.leaveType.text = getString(R.string.break_out_text)
            3 -> view.leaveType.text = getString(R.string.break_in_text)
            else -> view.leaveType.text = getString(R.string.time_out_text)
        }
        view.time.text = currentTime
        view.okBtn?.setOnClickListener {
            val mBundle = Bundle()
            val fragmentManager = myInterface?.getSupportFragmentManager()
            val fragment = LogsFragment()
            mBundle.putParcelable("keyAccountDetails", myDetails)
            fragment.arguments = mBundle
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
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
        val TAG: String = AddTimeLogSuccessFragment::class.java.simpleName
        fun newInstance(bundle: Bundle) = AddTimeLogSuccessFragment().apply {
            this.arguments = bundle
        }
    }
}
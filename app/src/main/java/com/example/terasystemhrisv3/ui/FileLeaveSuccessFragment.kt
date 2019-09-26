package com.example.terasystemhrisv3.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.terasystemhrisv3.interfaces.AppBarController
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails
import kotlinx.android.synthetic.main.fragment_fileleavesuccess.view.*

class FileLeaveSuccessFragment : Fragment() {

    private var myInterface: AppBarController? = null
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        var leaveType = ""
        var time = 0
        var startDate = ""
        var endDate = ""
        if (bundle != null)
        {
            leaveType = bundle.getString("typeOfLeave")!!
            time = bundle.getInt("time")
            startDate = bundle.getString("startDate")!!
            endDate = bundle.getString("endDate")!!
            myDetails = bundle.getParcelable("keyAccountDetails")!!
        }
        val view = inflater.inflate(R.layout.fragment_fileleavesuccess, container, false)
        myInterface?.setTitle(getString(R.string.fileleavesuccess_title))
        myInterface?.setAddButtonTitle(null)
        myInterface?.setCancelButtonTitle(null)
        myInterface?.getAddButton()?.visibility = View.GONE
        myInterface?.getCancelButton()?.visibility = View.GONE
        if(leaveType == "1")
        {
            view.leaveType.text = getString(R.string.vacation_leave_title)
        }
        else
        {
            view.leaveType.text = getString(R.string.sick_leave_title)
        }
        when (time) {
            1 -> view.time.text = "Whole Day"
            2 -> view.time.text = "Morning"
            else -> view.time.text = "Afternoon"
        }
        if(endDate == "" || endDate.isNullOrEmpty())
        {
            view.endDateTitle.visibility = View.GONE
            view.endDate.visibility = View.GONE
            view.startDateSuccessTitle.text = getString(R.string.file_leave_date)
            view.startDate.text = startDate
        }
        else
        {
            view.endDateTitle.visibility = View.VISIBLE
            view.endDate.visibility = View.VISIBLE
            view.startDate.text = startDate
            view.endDate.text = endDate
        }
        view.okBtn?.setOnClickListener {
            val mBundle = Bundle()
            val fragmentManager = myInterface?.getSupportFragmentManager()
            val fragment = LeavesFragment()
            mBundle.putParcelable("keyAccountDetails", myDetails)
            fragment.arguments = mBundle
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.container, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

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
        val TAG: String = FileLeaveSuccessFragment::class.java.simpleName
        fun newInstance(bundle: Bundle) = FileLeaveSuccessFragment().apply {
            this.arguments = bundle
        }
    }
}
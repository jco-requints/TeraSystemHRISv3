package com.example.terasystemhrisv3.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.terasystemhrisv3.interfaces.AppBarController
import com.example.terasystemhrisv3.interfaces.FragmentNavigator
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.viewmodel.AddTimeLogSuccessViewModel
import kotlinx.android.synthetic.main.fragment_addtimelogsuccess.view.*

class AddTimeLogSuccessFragment : Fragment() {

    private var myInterface: AppBarController? = null
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")
    private lateinit var addTimeLogSuccessViewModel: AddTimeLogSuccessViewModel
    private var fragmentNavigatorInterface: FragmentNavigator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        addTimeLogSuccessViewModel = ViewModelProviders.of(this).get(AddTimeLogSuccessViewModel::class.java)
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
        addTimeLogSuccessViewModel.intLogType.value = logType

        addTimeLogSuccessViewModel.intLogType.observe(viewLifecycleOwner, Observer {
            addTimeLogSuccessViewModel.convertLogTypeToWord()
        })

        addTimeLogSuccessViewModel.wordLogType.observe(viewLifecycleOwner, Observer {
            view.logType.text = it
        })

        view.time.text = currentTime
        view.okBtn?.setOnClickListener {
            val mBundle = Bundle()
            mBundle.putParcelable("keyAccountDetails", myDetails)
            fragmentNavigatorInterface?.showTimeLogs(mBundle, LogsFragment())
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is AppBarController)
        {
            myInterface = context
        }
        if(context is FragmentNavigator)
        {
            fragmentNavigatorInterface = context
        }
    }

    companion object {
        val TAG: String = AddTimeLogSuccessFragment::class.java.simpleName
        fun newInstance(bundle: Bundle) = AddTimeLogSuccessFragment().apply {
            this.arguments = bundle
        }
    }
}
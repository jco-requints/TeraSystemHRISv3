package com.example.terasystemhrisv3.ui

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.terasystemhrisv3.AppBarController
import com.example.terasystemhrisv3.FragmentNavigator
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import com.example.terasystemhrisv3.services.WebServiceConnection
import kotlinx.android.synthetic.main.fragment_addtimelog.view.*
import org.json.JSONObject
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddTimeLogFragment : Fragment(), NetworkRequestInterface {

    private var myInterface: AppBarController? = null
    private var fragmentNavigatorInterface: FragmentNavigator? = null
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        if (bundle != null)
        {
            myDetails = bundle.getParcelable("keyAccountDetails")!!
        }
        val view = inflater.inflate(R.layout.fragment_addtimelog, container, false)
        myInterface?.setTitle(getString(R.string.addtimelog_title))
        myInterface?.setAddButtonTitle(getString(R.string.done_title))
        myInterface?.setCancelButtonTitle(getString(R.string.cancel_title))
        myInterface?.getAddButton()?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        myInterface?.getCancelButton()?.visibility = View.VISIBLE
        myInterface?.getCancelButton()?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F)
        val adapter = ArrayAdapter.createFromResource(view.context,
            R.array.log_type, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.spinner?.adapter = adapter

        view.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedText: TextView = parent?.getChildAt(0) as TextView
                selectedText.setTextColor(Color.BLACK)
            }
        }

        myInterface?.getAddButton()?.setOnClickListener {
//            view?.progressBarHolder?.visibility = View.VISIBLE
            if (isConnected(container!!.context)) {
                val mURL = URL("http://222.222.222.71:9080/MobileAppTraining/AppTrainingAddTimeLog.htm").toString()
                val itemSelected = view.spinner.selectedItemPosition + 1
                WebServiceConnection(this).execute(mURL, myDetails.username, itemSelected.toString())
            }
            else
            {
                view.popupHolder.visibility = View.GONE
                view.network_status.text = getString(R.string.no_internet_message)
            }
        }

        view.txtclose?.setOnClickListener {
            view.popupHolder.visibility = View.GONE
        }

        myInterface?.getCancelButton()?.setOnClickListener {
            val fragmentManager = myInterface?.getSupportFragmentManager()
            fragmentManager?.popBackStackImmediate()
        }
        return view
    }

    override fun onAttach(context: Context?) {
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

    override fun beforeNetworkCall() {
        view?.progressBarHolder?.visibility = View.VISIBLE
    }

    override fun afterNetworkCall(result: String?) {
        view?.progressBarHolder?.visibility = View.GONE
        if(result == "Connection Timeout")
        {
            view?.popupHolder?.visibility = View.VISIBLE
            view?.network_status?.text = getString(R.string.connection_timeout_message)
        }
        else if(result.isNullOrEmpty())
        {
            view?.popupHolder?.visibility = View.VISIBLE
            view?.network_status?.text = getString(R.string.server_error)
        }
        else
        {
            val jsonObject = JSONObject(result)
            val status = jsonObject?.get("status").toString()
            if(status == "0")
            {
                val itemSelected = view?.spinner?.selectedItemPosition?.plus(1)
                val mBundle = Bundle()
                val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now()
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateTimeFormatter.ofPattern("h:mm a")
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                val formatted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    current.format(formatter)
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                mBundle.putInt("logType", itemSelected!!)
                mBundle.putString("currentTime", formatted)
                mBundle.putParcelable("keyAccountDetails", myDetails)
                fragmentNavigatorInterface?.showAddTimeLogSuccess(mBundle, AddTimeLogSuccessFragment())
            }
            else
            {
                view?.popupHolder?.visibility = View.VISIBLE
                view?.network_status?.text = getString(R.string.logs_update_error_message)
            }
        }
    }

    companion object {
        val TAG: String = AddTimeLogFragment::class.java.simpleName
        fun newInstance(bundle: Bundle) = AddTimeLogFragment().apply {
            this.arguments = bundle
        }
    }

    fun isConnected(context: Context): Boolean {
        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info)
                    if (i.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
        }
        return false
    }

}
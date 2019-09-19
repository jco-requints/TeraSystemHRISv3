package com.example.terasystemhrisv3.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terasystemhrisv3.AppBarController
import com.example.terasystemhrisv3.FragmentNavigator
import com.example.terasystemhrisv3.LeavesRecyclerAdapter
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.model.Leaves
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import com.example.terasystemhrisv3.services.WebServiceConnection
import kotlinx.android.synthetic.main.fragment_leaves.view.*
import org.json.JSONObject
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class LeavesFragment : Fragment(), NetworkRequestInterface {

    private var myInterface: AppBarController? = null
    private var fragmentNavigatorInterface: FragmentNavigator? = null
    lateinit var leavesList: Leaves
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: LeavesRecyclerAdapter
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        if (bundle != null)
        {
            myDetails = bundle.getParcelable("keyAccountDetails")!!
        }
        val view = inflater.inflate(R.layout.fragment_leaves, container, false)
        val mURL = URL("http://222.222.222.71:9080/MobileAppTraining/AppTrainingGetLeaves.htm").toString()
        myInterface?.setTitle(getString(R.string.leaves_title))
        myInterface?.setAddButtonTitle("+")
        myInterface?.setCancelButtonTitle(null)
        myInterface?.getAddButton()?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36F)
        myInterface?.getAddButton()?.visibility = View.VISIBLE
        myInterface?.getCancelButton()?.visibility = View.GONE

        if (isConnected(container!!.context)) {
            WebServiceConnection(this).execute(mURL, myDetails?.username)
        }
        else
        {
            view.popupHolder.visibility = View.VISIBLE
            view.network_status.text = getString(R.string.no_internet_message)
        }

        //Logic for + button
        myInterface?.getAddButton()?.setOnClickListener {
            val mBundle = Bundle()
            mBundle.putParcelable("keyAccountDetails", myDetails)
            fragmentNavigatorInterface?.showFileLeave(mBundle, FileLeaveFragment())
        }

        view.txtclose?.setOnClickListener {
            view.popupHolder.visibility = View.GONE
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
        view?.leavesProgressBarHolder?.visibility = View.VISIBLE
    }

    override fun afterNetworkCall(result: String?) {
        view?.leavesProgressBarHolder?.visibility = View.GONE
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
            if (status == "0") {
                val leaves = ArrayList<Leaves>()
                var remVL = 13.0
                var remSL = 13.0
                val jsonArray = jsonObject.getJSONArray("leaves")
                for (i in 0 until jsonArray.length()) {
                    leavesList = Leaves("", "", "", "", "")
                    val obj = jsonArray.getJSONObject(i)
                    leavesList.userID = obj.getString("userID")
                    leavesList.type = convertVLTypeToHumanForm(obj.getString("type"))
                    leavesList.dateFrom = convertDateToHumanDate(obj.getString("dateFrom"))
                    leavesList.dateTo = convertDateToHumanDate(obj.getString("dateTo"))
                    leavesList.time = convertTimeToHumanForm(obj.getString("time"), obj.getString("dateFrom"), obj.getString("dateTo"))
                    if(leavesList.type == "Vacation Leave")
                    {
                        remVL -= leavesList.time.toFloat()
                    }
                    else
                    {
                        remSL -= leavesList.time.toFloat()
                    }
                    leaves.add(leavesList)
                }

                view?.vacationLeave?.text = trimTrailingZero(remVL.toString())
                view?.sickLeave?.text = trimTrailingZero(remSL.toString())
                linearLayoutManager = LinearLayoutManager(this.context)
                view?.leavesRecyclerView?.layoutManager = linearLayoutManager
                adapter = LeavesRecyclerAdapter(leaves)
                view?.leavesRecyclerView?.adapter = adapter
                view?.leavesRecyclerView?.isNestedScrollingEnabled = false
            } else {
                view?.popupHolder?.visibility = View.VISIBLE
                view?.network_status?.text = getString(R.string.logs_error_message)
            }
        }
    }

    companion object {
        val TAG: String = LeavesFragment::class.java.simpleName
        fun newInstance(bundle: Bundle) = LeavesFragment().apply {
            this.arguments = bundle
        }
    }

    private fun convertDateToHumanDate(leaveDate: String): String {
        val humanDateFormat = SimpleDateFormat("MMMM d")
        val cal = Calendar.getInstance()
        try {
            if(leaveDate != "null")
            {
                val parsedDateFormat = Date(leaveDate.toLong())
                cal.time = parsedDateFormat
                return humanDateFormat.format(cal.time)
            }
            return ""
        } catch (e: ParseException) {
            e.printStackTrace()
            return ""
        }
    }

    private fun convertVLTypeToHumanForm(leaveType: String): String {
        var convertedLeaveType = ""
        try {
            if(leaveType == "1")
            {
                convertedLeaveType = "Vacation Leave"
            }
            else
            {
                convertedLeaveType = "Sick Leave"
            }
            return convertedLeaveType
        } catch (e: ParseException) {
            e.printStackTrace()
            return convertedLeaveType
        }
    }

    private fun convertTimeToHumanForm(time: String, dateFrom: String, dateTo: String): String {
        var convertedTime = ""
        var total: Float
        val humanDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val parsedDateFromFormat: Date
        val parsedDateToFormat: Date
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        try {
            if(time == "1")
            {
                if(dateTo == "null")
                {
                    convertedTime = "1"
                }
                else
                {
                    parsedDateFromFormat = Date(dateFrom.toLong())
                    parsedDateToFormat = Date(dateTo.toLong())
                    cal1.time = parsedDateFromFormat
                    cal2.time = parsedDateToFormat

                    val date1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDate.parse(humanDateFormat.format(cal1.time), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                    val date2 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDate.parse(humanDateFormat.format(cal2.time), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }

                    convertedTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Period.between(date1, date2).toString()
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                    convertedTime = convertedTime.substring(1, convertedTime.length -1)
                    total = convertedTime.toFloat() + 1
                    convertedTime = trimTrailingZero(total.toString())
                }
            }
            else
            {
                convertedTime = "0.5"
            }
            return convertedTime
        } catch (e: ParseException) {
            e.printStackTrace()
            return convertedTime
        }
    }

    fun trimTrailingZero(value: String): String {
        return if (!value.isNullOrEmpty()) {
            if (value!!.indexOf(".") < 0) {
                value

            } else {
                value.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
            }

        } else {
            value
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
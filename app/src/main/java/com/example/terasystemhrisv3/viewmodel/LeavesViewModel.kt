package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.service.WebServiceConnection
import com.example.terasystemhrisv3.interfaces.NetworkRequestInterface
import com.example.terasystemhrisv3.model.Leaves
import com.example.terasystemhrisv3.util.*
import org.json.JSONObject
import java.net.URLEncoder
import java.text.ParseException
import kotlin.collections.ArrayList
import java.util.concurrent.TimeUnit


class LeavesViewModel(application: Application) : AndroidViewModel(application), NetworkRequestInterface {

    var webServiceError = SingleLiveEvent<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var leaves = MutableLiveData<Leaves>()
    var remVL: Double = 0.0
    var remSL: Double = 0.0
    lateinit var leavesHolder: Leaves
    var leavesList = MutableLiveData<ArrayList<Leaves>>()
    private val leavesListHolder = ArrayList<Leaves>()
    var showProgressbar = MutableLiveData<Boolean>()
    var showRemSLAndRemVL = MutableLiveData<Boolean>()
    var showRecyclerView = MutableLiveData<Boolean>()
    var isFileLeaveClicked = MutableLiveData<Boolean>()

    init {
        showRecyclerView.value = false
        showRemSLAndRemVL.value = false
        showProgressbar.value = false
        isFileLeaveClicked.value = false
    }

    fun getLeaves(){
        if(isConnected(getApplication()))
        {
            val reqParam = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(accountDetails.value?.userID, "UTF-8")
            WebServiceConnection(this).execute(URLs.URL_GET_LEAVES, reqParam)
        }
        else
        {
            leavesList.value?.clear()
            leavesListHolder.clear()
            showRecyclerView.value = false
            showRemSLAndRemVL.value = false
            webServiceError.value = "No Internet Connection"
        }
    }

    override fun beforeNetworkCall() {
        leavesList.value?.clear()
        leavesListHolder.clear()
        showRecyclerView.value = false
        showRemSLAndRemVL.value = false
        showProgressbar.value = true
    }

    override fun afterNetworkCall(result: String?){
        showProgressbar.value = false
        try {
            remVL = 13.0
            remSL = 13.0
            val jsonObject = JSONObject(result!!)
            val status = jsonObject.get("status").toString()
            if(status == "0")
            {
                val jsonArray = jsonObject.getJSONArray("leaves")
                for (i in 0 until jsonArray.length()) {
                    leavesHolder = Leaves("","","","","")
                    val obj = jsonArray.getJSONObject(i)
                    leavesHolder.userID = obj.getString("userID")
                    leavesHolder.type = convertLeaveTypeToReadableForm(obj.getString("type"))
                    leavesHolder.dateFrom = convertDateToHumanDate(obj.getString("dateFrom"))
                    leavesHolder.dateTo = convertDateToHumanDate(obj.getString("dateTo"))
                    leavesHolder.time = convertTimeToReadableForm(obj.getString("time"), obj.getString("dateFrom"), obj.getString("dateTo"))
                    if(leavesHolder.type == "Vacation Leave")
                    {
                        remVL -= leavesHolder.time.toFloat()
                    }
                    else
                    {
                        remSL -= leavesHolder.time.toFloat()
                    }
                    leaves.value = leavesHolder
                    leavesListHolder.add(leaves.value!!)
                }
                showRecyclerView.value = true
                showRemSLAndRemVL.value = true
                leavesList.value = leavesListHolder
            }
            else
            {
                webServiceError.value = jsonObject.get("message").toString()
            }
        } catch (ex: Exception){
            webServiceError.value = result
        }
    }

    fun convertLeaveTypeToReadableForm(leaveType: String): String {
        var convertedLeaveType = ""
        return try {
            convertedLeaveType = if(leaveType == "1") {
                "Vacation Leave"
            } else {
                "Sick Leave"
            }
            convertedLeaveType
        } catch (e: ParseException) {
            e.printStackTrace()
            convertedLeaveType
        }
    }

    fun convertTimeToReadableForm(time: String, dateFrom: String, dateTo: String): String {
        var convertedTime = ""
        val total: Float
        try {
            if(time == "1")
            {
                if(dateTo == "null")
                {
                    convertedTime = "1"
                }
                else
                {

                    val diffInMillisec = dateTo.toLong() - dateFrom.toLong()

                    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillisec)

                    total = diffInDays.toFloat() + 1
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
}
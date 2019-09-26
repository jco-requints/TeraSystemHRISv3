package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.util.URLs
import com.example.terasystemhrisv3.util.isConnected
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.service.WebServiceConnection
import com.example.terasystemhrisv3.interfaces.NetworkRequestInterface
import com.example.terasystemhrisv3.model.Leaves
import com.example.terasystemhrisv3.util.SingleLiveEvent
import org.json.JSONObject
import java.net.URLEncoder
import java.text.ParseException

class LeavesViewModel(application: Application) : AndroidViewModel(application), NetworkRequestInterface {

    var webServiceError = SingleLiveEvent<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var leaves = MutableLiveData<Leaves>()
    lateinit var leavesHolder: Leaves
    var leavesList = MutableLiveData<ArrayList<Leaves>>()
    private val leavesListHolder = ArrayList<Leaves>()
    var showProgressbar = MutableLiveData<Boolean>()
    var isFileLeaveClicked = MutableLiveData<Boolean>()

    init {
        showProgressbar.value = false
        isFileLeaveClicked.value = false
    }

    fun getLeaves(){
        if(isConnected(getApplication()))
        {
            val reqParam = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(accountDetails.value?.username, "UTF-8")
            WebServiceConnection(this).execute(URLs.URL_GET_TIME_LOGS, reqParam)
        }
        else
        {
            webServiceError.value = "No Internet Connection"
        }
    }

    fun showFileLeave(){
        isFileLeaveClicked.value = true
    }

    private fun convertLeaveTypeToReadableForm(leaveType: String): String {
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

    override fun beforeNetworkCall() {
        showProgressbar.value = true
    }

    override fun afterNetworkCall(result: String?){
        showProgressbar.value = false
        try {
            val jsonObject = JSONObject(result!!)
            val status = jsonObject.get("status").toString()
            if(status == "0")
            {
                leavesListHolder.clear()
                val jsonArray = jsonObject.getJSONArray("timeLogs")
                for (i in 0 until jsonArray.length()) {
                    leavesHolder = Leaves("","","","","")
                    val obj = jsonArray.getJSONObject(i)
                    leavesHolder.userID = obj.getString("userID")
                    leavesHolder.type = convertLeaveTypeToReadableForm(obj.getString("type"))
//                    leavesHolder.dateFrom = convertTimeToStandardTime(obj.getString("dateFrom"))
//                    leavesHolder.dateTo = convertTimeToStandardTime(obj.getString("dateTo"))
//                    leavesHolder.time = convertTimeToStandardTime(obj.getString("time"), obj.getString("dateFrom"), obj.getString("dateTo"))
                    leaves.value = leavesHolder
                    leavesListHolder.add(leaves.value!!)
                }
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
}
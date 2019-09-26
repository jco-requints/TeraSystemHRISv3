package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.model.Logs
import com.example.terasystemhrisv3.service.WebServiceConnection
import com.example.terasystemhrisv3.interfaces.NetworkRequestInterface
import com.example.terasystemhrisv3.util.*
import org.json.JSONObject
import java.net.URLEncoder
import java.util.ArrayList

class LogsViewModel(application: Application) : AndroidViewModel(application), NetworkRequestInterface {

    var webServiceError = SingleLiveEvent<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var logs = MutableLiveData<Logs>()
    lateinit var logsHolder: Logs
    var logsList = MutableLiveData<ArrayList<Logs>>()
    val logsListHolder = ArrayList<Logs>()
    var showProgressbar = MutableLiveData<Boolean>()
    var isAddTimeLogClicked = MutableLiveData<Boolean>()

    init {
        showProgressbar.value = false
        isAddTimeLogClicked.value = false
    }

    fun getTimeLogs(){
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

    fun showAddTimeLog(){
        isAddTimeLogClicked.value = true
    }

    override fun beforeNetworkCall() {
        logsList.value?.clear()
        showProgressbar.value = true
    }

    override fun afterNetworkCall(result: String?){
        showProgressbar.value = false
        try {
            val jsonObject = JSONObject(result!!)
            val status = jsonObject.get("status").toString()
            if(status == "0")
            {
                logsListHolder.clear()
                val jsonArray = jsonObject.getJSONArray("timeLogs")
                for (i in 0 until jsonArray.length()) {
                    logsHolder = Logs("","","","","","")
                    val obj = jsonArray.getJSONObject(i)
                    logsHolder.userID = obj.getString("userID")
                    logsHolder.date = convertDateToHumanDate(obj.getString("date"))
                    logsHolder.timeIn = convertTimeToStandardTime(obj.getString("timeIn"))
                    logsHolder.breakOut = convertTimeToStandardTime(obj.getString("breakOut"))
                    logsHolder.breakIn = convertTimeToStandardTime(obj.getString("breakIn"))
                    logsHolder.timeOut = convertTimeToStandardTime(obj.getString("timeOut"))
                    logs.value = logsHolder
                    logsListHolder.add(logs.value!!)
                }
                logsList.value = logsListHolder
//                if adding an element
//                logsList.value = logsList.value.apply {
//
//                }
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
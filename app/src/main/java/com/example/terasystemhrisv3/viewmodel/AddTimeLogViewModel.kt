package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.URLs
import com.example.terasystemhrisv3.isConnected
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import com.example.terasystemhrisv3.services.WebServiceConnection
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder


class AddTimeLogViewModel(application: Application) : AndroidViewModel(application), NetworkRequestInterface {

    var webServiceError = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var showProgressbar = MutableLiveData<Boolean>()
    var selectedItem = MutableLiveData<Int>()
    var isAddTimeLogSuccesful = MutableLiveData<Boolean>()

    init {
        isAddTimeLogSuccesful.value = false
        showProgressbar.value = false
    }

    fun addTimeLog() {
        if (isConnected(getApplication()))
        {
            val reqParam = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(accountDetails.value?.username, "UTF-8")
            val mURL = URL(URLs.URL_ADD_TIME_LOG).toString()
            WebServiceConnection(this).execute(mURL, reqParam, selectedItem.value.toString())
        }
        else
        {
            webServiceError.value = "No Internet Connection"
        }
    }

    override fun beforeNetworkCall() {
        showProgressbar.value = true
    }

    override fun afterNetworkCall(result: String?) {
        showProgressbar.value = false
        try {
            val jsonObject = JSONObject(result!!)
            val status = jsonObject.get("status").toString()
            if(status == "0")
            {
                isAddTimeLogSuccesful.value = true
            }
            else
            {
                webServiceError.value = jsonObject.get("message").toString()
                isAddTimeLogSuccesful.value = false
            }
        } catch (ex: Exception){
            webServiceError.value = result
            isAddTimeLogSuccesful.value = false
        }
    }
}
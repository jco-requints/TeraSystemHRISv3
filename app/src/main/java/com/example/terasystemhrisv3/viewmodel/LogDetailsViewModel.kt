package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.*
import com.example.terasystemhrisv3.*
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.model.Logs
import com.example.terasystemhrisv3.services.WebServiceConnection
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import org.json.JSONObject
import java.net.URLEncoder
import java.util.ArrayList

class LogDetailsViewModel(application: Application) : AndroidViewModel(application) {

    var isTimeInEmpty = MutableLiveData<Boolean>()
    var isTimeOutEmpty = MutableLiveData<Boolean>()
    var isBreakInEmpty = MutableLiveData<Boolean>()
    var isBreakOutEmpty = MutableLiveData<Boolean>()

    init {
        isTimeInEmpty.value = false
        isTimeOutEmpty.value = false
        isBreakInEmpty.value = false
        isBreakOutEmpty.value = false
    }

    fun loadLogDetails(itemTimeIn: String, itemTimeOut: String, itemBreakIn: String, itemBreakOut: String){
        if(isFieldNullOrEmpty(itemTimeIn))
        {
            isTimeInEmpty.value = true
        }
        if(isFieldNullOrEmpty(itemTimeOut))
        {
            isTimeOutEmpty.value = true
        }
        if(isFieldNullOrEmpty(itemBreakIn))
        {
            isBreakInEmpty.value = true
        }
        if(isFieldNullOrEmpty(itemBreakOut))
        {
            isBreakOutEmpty.value = true
        }
    }

}
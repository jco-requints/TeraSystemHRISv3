package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import com.example.terasystemhrisv3.URLs
import com.example.terasystemhrisv3.isConnected
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import com.example.terasystemhrisv3.services.WebServiceConnection
import com.example.terasystemhrisv3.ui.AddTimeLogSuccessFragment
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder
import java.util.*


class AddTimeLogSuccessViewModel(application: Application) : AndroidViewModel(application) {

    var intLogType = MutableLiveData<Int>()
    var wordLogType = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var showProgressbar = MutableLiveData<Boolean>()
    var selectedItem = MutableLiveData<Int>()
    var isAddTimeLogSuccesful = MutableLiveData<Boolean>()

    init {
        isAddTimeLogSuccesful.value = false
        showProgressbar.value = false
    }

    fun convertLogTypeToWord() {
        when (intLogType.value) {
            1 -> wordLogType.value = "Time In"
            2 -> wordLogType.value = "Break Out"
            3 -> wordLogType.value = "Break In"
            else -> wordLogType.value = "Time Out"
        }
    }
}
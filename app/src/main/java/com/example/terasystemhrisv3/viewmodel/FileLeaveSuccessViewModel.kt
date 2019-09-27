package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.*
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.service.WebServiceConnection
import com.example.terasystemhrisv3.interfaces.NetworkRequestInterface
import com.example.terasystemhrisv3.model.Leaves
import com.example.terasystemhrisv3.util.*
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import java.util.concurrent.TimeUnit


class FileLeaveSuccessViewModel(application: Application) : AndroidViewModel(application) {

    var leaveType = MutableLiveData<String>()
    var wordLeaveType = MutableLiveData<String>()
    var intTimeType = MutableLiveData<Int>()
    var wordTimeType = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()

    init {

    }

    fun convertLeaveTypeToWord() {
        when (leaveType.value) {
            "1" -> wordLeaveType.value = "Vacation Leave"
            else -> wordLeaveType.value = "Sick Leave"
        }
    }

    fun convertTimeTypeToWord() {
        when (intTimeType.value) {
            1 -> wordTimeType.value = "Whole Day"
            2 -> wordTimeType.value = "Morning"
            else -> wordTimeType.value = "Afternoon"
        }
    }
}
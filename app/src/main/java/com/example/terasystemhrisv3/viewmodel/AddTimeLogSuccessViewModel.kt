package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.model.AccountDetails


class AddTimeLogSuccessViewModel(application: Application) : AndroidViewModel(application) {

    var intLogType = MutableLiveData<Int>()
    var wordLogType = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()

    init {

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
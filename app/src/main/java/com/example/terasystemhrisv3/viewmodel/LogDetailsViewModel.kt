package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.util.isFieldNullOrEmpty

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
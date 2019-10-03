package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.model.Logs
import com.example.terasystemhrisv3.service.RetrofitFactory
import com.example.terasystemhrisv3.util.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.util.ArrayList

class LogsViewModel(application: Application) : AndroidViewModel(application) {

    var webServiceError = SingleLiveEvent<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var logs = MutableLiveData<Logs>()
    lateinit var logsHolder: Logs
    private val job = SupervisorJob()
    private val coroutineContext = Dispatchers.IO + job
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
            showProgressbar.value = true
            val service = RetrofitFactory.makeRetrofitService()
            logsList.value?.clear()
            logsListHolder.clear()
            CoroutineScope(coroutineContext).launch {
                val response = service.GetTimeLogs(accountDetails.value?.userID)
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            val details = response.body()
                            if(details?.status == "0")
                            {
                                for (i in 0 until details.timeLogs!!.count()) {
                                    logsHolder = Logs("","","","","","")
                                    logsHolder.userID = details.timeLogs!![i].userID
                                    logsHolder.date = details.timeLogs!![i].date?.let { date -> convertDateToHumanDate(date) }
                                    logsHolder.timeIn = details.timeLogs!![i].timeIn?.let { timeIn -> convertTimeToStandardTime(timeIn) }
                                    logsHolder.breakOut = details.timeLogs!![i].timeOut?.let { timeOut -> convertTimeToStandardTime(timeOut) }
                                    logsHolder.breakIn = details.timeLogs!![i].breakIn?.let { breakIn -> convertTimeToStandardTime(breakIn) }
                                    logsHolder.timeOut = details.timeLogs!![i].breakOut?.let { breakOut -> convertTimeToStandardTime(breakOut) }
                                    logs.value = logsHolder
                                    logsListHolder.add(logs.value!!)
                                }
                                logsList.value = logsListHolder
                            }
                            else
                            {
                                webServiceError.value = response.body()?.message
                            }
                            showProgressbar.postValue(false)
                        } else {
                            webServiceError.postValue("Error: ${response.code()}")
                            showProgressbar.postValue(false)
                        }
                    } catch (e: HttpException) {
                        webServiceError.postValue("Exception ${e.message}")
                        showProgressbar.postValue(false)
                    } catch (e: Throwable) {
                        webServiceError.postValue(e.message)
                        showProgressbar.postValue(false)
                    }
                }
            }
        }
        else
        {
            logsList.value?.clear()
            logsListHolder.clear()
            webServiceError.value = "No Internet Connection"
        }
    }

    fun showAddTimeLog(){
        isAddTimeLogClicked.value = true
    }

}
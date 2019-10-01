package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.model.Logs
import com.example.terasystemhrisv3.service.WebServiceConnection
import com.example.terasystemhrisv3.interfaces.NetworkRequestInterface
import com.example.terasystemhrisv3.model.GsonAccountDetails
import com.example.terasystemhrisv3.model.GsonLogs
import com.example.terasystemhrisv3.service.RetrofitFactory
import com.example.terasystemhrisv3.util.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.HttpException
import java.net.URLEncoder
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
//            val service = RetrofitFactory.makeRetrofitService()
//            CoroutineScope(coroutineContext).launch {
//                val response = service.GetTimeLogs(accountDetails.value?.userID)
//                withContext(Dispatchers.Main) {
//                    try {
//                        if (response.isSuccessful) {
//                            val jsonObj = JSONObject(Gson().toJson(response.body()))
//                            if(response.body()!!.status == "0")
//                            {
//                                val jsonArray = jsonObj.getJSONArray("timeLogs")
//                                val gs = Gson()
//                                val details = gs.fromJson(jsonObj.toString(), GsonLogs::class.java)
//
//                                for (i in 0 until jsonArray.length()) {
//                                    logsHolder = Logs("","","","","","")
//                                    val obj = jsonArray.getJSONObject(i)
//                                    logsHolder.userID = details.userID!!
//                                    logsHolder.date = convertDateToHumanDate(details.date!!)
//                                    logsHolder.timeIn = convertTimeToStandardTime(details.timeIn!!)
//                                    logsHolder.breakOut = convertTimeToStandardTime(details.breakOut!!)
//                                    logsHolder.breakIn = convertTimeToStandardTime(details.breakIn!!)
//                                    logsHolder.timeOut = convertTimeToStandardTime(details.timeOut!!)
//                                    logs.value = logsHolder
//                                    logsListHolder.add(logs.value!!)
//                                }
//                                logsList.value = logsListHolder
//                            }
//                            else
//                            {
//                                webServiceError.value = response.body()?.message
//                            }
//                            showProgressbar.postValue(false)
//                        } else {
//                            webServiceError.postValue("Error: ${response.code()}")
//                            showProgressbar.postValue(false)
//                        }
//                    } catch (e: HttpException) {
//                        webServiceError.postValue("Exception ${e.message}")
//                        showProgressbar.postValue(false)
//                    } catch (e: Throwable) {
//                        webServiceError.postValue(e.message)
//                        showProgressbar.postValue(false)
//                    }
//                }
//            }
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
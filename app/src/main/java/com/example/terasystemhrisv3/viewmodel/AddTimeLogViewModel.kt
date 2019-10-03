package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.util.isConnected
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.service.RetrofitFactory
import kotlinx.coroutines.*
import retrofit2.HttpException

class AddTimeLogViewModel(application: Application) : AndroidViewModel(application) {

    var webServiceError = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var showProgressbar = MutableLiveData<Boolean>()
    var selectedItem = MutableLiveData<Int>()
    private val job = SupervisorJob()
    private val coroutineContext = Dispatchers.IO + job
    var isAddTimeLogSuccesful = MutableLiveData<Boolean>()

    init {
        isAddTimeLogSuccesful.value = false
        showProgressbar.value = false
    }

    fun addTimeLog() {
        if (isConnected(getApplication()))
        {
            showProgressbar.value = true
            val service = RetrofitFactory.makeRetrofitService()
            CoroutineScope(coroutineContext).launch {
                val response = service.AddTimeLog(accountDetails.value?.userID, selectedItem.value.toString())
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            val details = response.body()
                            if(details?.status == "0")
                            {
                                isAddTimeLogSuccesful.value = true
                            }
                            else
                            {
                                webServiceError.value = response.body()?.message
                                isAddTimeLogSuccesful.value = false
                            }
                            showProgressbar.postValue(false)
                        } else {
                            webServiceError.postValue("Error: ${response.code()}")
                            showProgressbar.postValue(false)
                            isAddTimeLogSuccesful.value = false
                        }
                    } catch (e: HttpException) {
                        webServiceError.postValue("Exception ${e.message}")
                        showProgressbar.postValue(false)
                        isAddTimeLogSuccesful.value = false
                    } catch (e: Throwable) {
                        webServiceError.postValue(e.message)
                        showProgressbar.postValue(false)
                        isAddTimeLogSuccesful.value = false
                    }
                }
            }
        }
        else
        {
            webServiceError.value = "No Internet Connection"
        }
    }
}
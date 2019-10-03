package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.service.RetrofitFactory
import com.example.terasystemhrisv3.util.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.text.SimpleDateFormat


class FileLeaveViewModel(application: Application) : AndroidViewModel(application) {

    var webServiceError = SingleLiveEvent<String>()
    var selectedTypeOfLeave = MutableLiveData<String>()
    var selectedItem = MutableLiveData<Int>()
    var startDate = MutableLiveData<String>()
    var endDate = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    private val job = SupervisorJob()
    private val coroutineContext = Dispatchers.IO + job
    var showProgressbar = MutableLiveData<Boolean>()
    var isFileLeaveSuccesful = MutableLiveData<Boolean>()

    init {
        selectedTypeOfLeave.value = "1"
        showProgressbar.value = false
        isFileLeaveSuccesful.value = false
    }

    fun fileLeave(){
        if(isConnected(getApplication())) {
            var isDateValid = true
            if(!isFieldNullOrEmpty(endDate.value.toString()))
            {
                isDateValid = isDateValid(startDate.value.toString(), endDate.value.toString())
            }
            if(!isFieldNullOrEmpty(endDate.value.toString()) && isDateValid && !isFieldNullOrEmpty(selectedTypeOfLeave.value.toString()))
            {
                showProgressbar.value = true
                val service = RetrofitFactory.makeRetrofitService()
                CoroutineScope(coroutineContext).launch {
                    val response = service.AddLeaveWholeDay(accountDetails.value?.userID.toString(), selectedTypeOfLeave.value.toString(), convertDateToStandardForm(startDate.value.toString()), convertDateToStandardForm(endDate.value.toString()), selectedItem.value.toString())
                    withContext(Dispatchers.Main) {
                        try {
                            if (response.isSuccessful) {
                                val details = response.body()
                                if(details?.status == "0")
                                {
                                    isFileLeaveSuccesful.value = true
                                }
                                else
                                {
                                    webServiceError.value = response.body()?.message
                                    isFileLeaveSuccesful.value = false
                                }
                                showProgressbar.postValue(false)
                            } else {
                                webServiceError.postValue("Error: ${response.code()}")
                                showProgressbar.postValue(false)
                                isFileLeaveSuccesful.value = false
                            }
                        } catch (e: HttpException) {
                            webServiceError.postValue("Exception ${e.message}")
                            showProgressbar.postValue(false)
                            isFileLeaveSuccesful.value = false
                        } catch (e: Throwable) {
                            webServiceError.postValue(e.message)
                            showProgressbar.postValue(false)
                            isFileLeaveSuccesful.value = false
                        }
                    }
                }
            }
            else if(!isFieldNullOrEmpty(selectedTypeOfLeave.value.toString()) && isFieldNullOrEmpty(endDate.value.toString()))
            {
                showProgressbar.value = true
                val service = RetrofitFactory.makeRetrofitService()
                CoroutineScope(coroutineContext).launch {
                    val response = service.AddLeaveHalfDay(accountDetails.value?.userID.toString(), selectedTypeOfLeave.value.toString(), convertDateToStandardForm(startDate.value.toString()), selectedItem.value.toString())
                    withContext(Dispatchers.Main) {
                        try {
                            if (response.isSuccessful) {
                                val details = response.body()
                                if(details?.status == "0")
                                {
                                    isFileLeaveSuccesful.value = true
                                }
                                else
                                {
                                    webServiceError.value = response.body()?.message
                                    isFileLeaveSuccesful.value = false
                                }
                                showProgressbar.postValue(false)
                            } else {
                                webServiceError.postValue("Error: ${response.code()}")
                                showProgressbar.postValue(false)
                                isFileLeaveSuccesful.value = false
                            }
                        } catch (e: HttpException) {
                            webServiceError.postValue("Exception ${e.message}")
                            showProgressbar.postValue(false)
                            isFileLeaveSuccesful.value = false
                        } catch (e: Throwable) {
                            webServiceError.postValue(e.message)
                            showProgressbar.postValue(false)
                            isFileLeaveSuccesful.value = false
                        }
                    }
                }
            }
            else if(isFieldNullOrEmpty(selectedTypeOfLeave.value.toString()))
            {
                webServiceError.value = "Please choose leave type"
            }
            else if(!isFieldNullOrEmpty(endDate.value.toString()) && !isDateValid)
            {
                webServiceError.value = "Invalid start and end Date"
            }
        }
        else
        {
            webServiceError.value = "No Internet Connection"
        }
    }

    private fun convertDateToStandardForm(date: String): String {
        val currentFormatOfDateToConvert = SimpleDateFormat("MMMM d, yyyy")
        val formatToBeConvertedInto = SimpleDateFormat("yyyy-MM-dd")
        try {
            val stringParsedToDate = currentFormatOfDateToConvert.parse(date)
            return formatToBeConvertedInto.format(stringParsedToDate)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    private fun isDateValid(startDate: String, endDate: String): Boolean {
        val currentFormatOfDateToConvert = SimpleDateFormat("MMMM d, yyyy")
        val startDateParsedToDate = currentFormatOfDateToConvert.parse(startDate)
        val endDateParsedToDate = currentFormatOfDateToConvert.parse(endDate)
        if(startDateParsedToDate.before(endDateParsedToDate) || (startDateParsedToDate.compareTo(endDateParsedToDate) == 0))
        {
            return true
        }
        return false
    }
}
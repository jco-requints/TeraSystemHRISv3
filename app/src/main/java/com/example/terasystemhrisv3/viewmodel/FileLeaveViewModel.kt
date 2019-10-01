package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.service.WebServiceConnection
import com.example.terasystemhrisv3.interfaces.NetworkRequestInterface
import com.example.terasystemhrisv3.util.*
import org.json.JSONObject
import java.net.URLEncoder
import java.text.SimpleDateFormat


class FileLeaveViewModel(application: Application) : AndroidViewModel(application), NetworkRequestInterface {

    var webServiceError = SingleLiveEvent<String>()
    var selectedTypeOfLeave = MutableLiveData<String>()
    var selectedItem = MutableLiveData<Int>()
    var startDate = MutableLiveData<String>()
    var endDate = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
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
                var reqParam = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(accountDetails.value?.userID, "UTF-8")
                reqParam += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(selectedTypeOfLeave.value, "UTF-8")
                reqParam += "&" + URLEncoder.encode("dateFrom", "UTF-8") + "=" + URLEncoder.encode(convertDateToStandardForm(startDate.value.toString()), "UTF-8")
                reqParam += "&" + URLEncoder.encode("dateTo", "UTF-8") + "=" + URLEncoder.encode(convertDateToStandardForm(endDate.value.toString()), "UTF-8")
                reqParam += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(selectedItem.value.toString(), "UTF-8")
                WebServiceConnection(this).execute(URLs.URL_ADD_LEAVE, reqParam)
            }
            else if(!isFieldNullOrEmpty(selectedTypeOfLeave.value.toString()) && isFieldNullOrEmpty(endDate.value.toString()))
            {
                var reqParam = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(accountDetails.value?.userID, "UTF-8")
                reqParam += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(selectedTypeOfLeave.value, "UTF-8")
                reqParam += "&" + URLEncoder.encode("dateFrom", "UTF-8") + "=" + URLEncoder.encode(startDate.value, "UTF-8")
                reqParam += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(selectedItem.value.toString(), "UTF-8")
                WebServiceConnection(this).execute(URLs.URL_ADD_LEAVE, reqParam)
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

    override fun beforeNetworkCall() {
        showProgressbar.value = true
    }

    override fun afterNetworkCall(result: String?){
        showProgressbar.value = false
        try {
            val jsonObject = JSONObject(result!!)
            val status = jsonObject.get("status").toString()
            if(status == "0")
            {
                isFileLeaveSuccesful.value = true
            }
            else
            {
                webServiceError.value = jsonObject.get("message").toString()
                isFileLeaveSuccesful.value = false
            }
        } catch (ex: Exception){
            webServiceError.value = result
            isFileLeaveSuccesful.value = false
        }
    }
}
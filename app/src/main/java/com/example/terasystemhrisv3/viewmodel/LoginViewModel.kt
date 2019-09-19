package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.URLs
import com.example.terasystemhrisv3.isConnected
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.services.WebServiceConnection
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import org.json.JSONObject
import java.net.URLEncoder

class LoginViewModel(application: Application) : AndroidViewModel(application), NetworkRequestInterface {

    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var loginError = MutableLiveData<String>()
    var webServiceError = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    lateinit var accountDetailsHolder: AccountDetails
    var showProgressbar = MutableLiveData<Boolean>()
    val areFieldsEmpty: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        username.value = ""
        password.value = ""
        showProgressbar.value = false
        areFieldsEmpty.addSource(username) {
            areFieldsEmpty.value = checkForEmptyFields()
        }
        areFieldsEmpty.addSource(password) {
            areFieldsEmpty.value = checkForEmptyFields()
        }
    }

    fun login(){
        var reqParam = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(username.value, "UTF-8")
        reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password.value, "UTF-8")
        if(isConnected(getApplication()))
        {
            WebServiceConnection(this).execute(URLs.URL_LOGIN, reqParam)
        }
        else
        {
            webServiceError.value = "No Internet Connection"
        }
    }

    override fun beforeNetworkCall() {
        showProgressbar.value = true
    }

    override fun afterNetworkCall(result: String?){
        showProgressbar.value = false
        try {
            var jsonObject = JSONObject(result!!)
            val status = jsonObject.get("status").toString()
            if(status == "0")
            {
                accountDetailsHolder = AccountDetails("","","","","","","","")
                jsonObject = jsonObject.getJSONObject("user")
                accountDetailsHolder.username = jsonObject?.get("userID").toString()
                accountDetailsHolder.empID = jsonObject?.get("idNumber").toString()
                accountDetailsHolder.firstName = jsonObject?.get("firstName").toString()
                accountDetailsHolder.middleName = jsonObject?.get("middleName").toString()
                accountDetailsHolder.lastName = jsonObject?.get("lastName").toString()
                accountDetailsHolder.emailAddress = jsonObject?.get("emailAddress").toString()
                accountDetailsHolder.mobileNumber = jsonObject?.get("mobileNumber").toString()
                accountDetailsHolder.landlineNumber = jsonObject?.get("landline").toString()
                accountDetails.value = accountDetailsHolder
            }
            else
            {
                loginError.value = jsonObject.get("message").toString()
            }
        } catch (ex: Exception){
            webServiceError.value = result
        }

    }

    private fun checkForEmptyFields() : Boolean {
        if(!username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()) return true
        return false
    }

}
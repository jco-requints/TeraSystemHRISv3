package com.example.terasystemhrisv3.viewmodel

import androidx.lifecycle.*
import com.example.terasystemhrisv3.URLs
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.services.WebServiceConnection
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import java.net.URLEncoder

class LoginViewModel : ViewModel(), NetworkRequestInterface {

    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var error = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var progressbar = MutableLiveData<Boolean>()
    val areFieldsEmpty: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        username.value = ""
        password.value = ""
        error.value = ""
        progressbar.value = false
        accountDetails = MutableLiveData()
        areFieldsEmpty.addSource(username) {
            areFieldsEmpty.value = checkForEmptyFields()
        }
        areFieldsEmpty.addSource(password) {
            areFieldsEmpty.value = checkForEmptyFields()
        }
    }

    fun login(): MutableLiveData<String>{
        var reqParam = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(username.value, "UTF-8")
        reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password.value, "UTF-8")
        WebServiceConnection(this).execute(URLs.URL_LOGIN, reqParam)
        return error
    }

    override fun beforeNetworkCall() {
        progressbar.value = true
    }

    override fun afterNetworkCall(result: String?){
        progressbar.value = false
        error.value = result
    }

    private fun checkForEmptyFields() : Boolean {
        if(!username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()) return true
        return false
    }

}
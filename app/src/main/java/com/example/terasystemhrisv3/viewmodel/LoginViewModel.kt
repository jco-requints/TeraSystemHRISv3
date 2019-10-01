package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.util.isConnected
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.model.GsonAccountDetails
import com.example.terasystemhrisv3.service.RetrofitFactory
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import org.json.JSONObject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var loginError = MutableLiveData<String>()
    var webServiceError = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    lateinit var accountDetailsHolder: AccountDetails
    private val job = SupervisorJob()
    private val coroutineContext = Dispatchers.IO + job
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
        if(isConnected(getApplication()))
        {
            showProgressbar.value = true
            val service = RetrofitFactory.makeRetrofitService()
            CoroutineScope(coroutineContext).launch {
                val response = service.Login(username.value, password.value)
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            val gs = Gson()
                            val details = response.body()
                            if(details?.status == "0")
                            {
                                accountDetailsHolder = AccountDetails("","","","","","","","")
                                accountDetailsHolder.userID = details.user?.userID!!
                                accountDetailsHolder.idNumber = details.user?.idNumber!!
                                accountDetailsHolder.firstName = details.user?.firstName!!
                                accountDetailsHolder.middleName = details.user?.middleName
                                accountDetailsHolder.lastName = details.user?.lastName!!
                                accountDetailsHolder.emailAddress = details.user?.emailAddress!!
                                accountDetailsHolder.mobileNumber = details.user?.mobileNumber!!
                                accountDetailsHolder.landlineNumber = details.user?.landlineNumber
                                accountDetails.value = accountDetailsHolder
                            }
                            else
                            {
                                loginError.value = response.body()!!.message
                            }
                            showProgressbar.postValue(false)
                        } else {
                            loginError.postValue("Error: ${response.code()}")
                            showProgressbar.postValue(false)
                        }
                    } catch (e: HttpException) {
                        loginError.postValue("Exception ${e.message}")
                        showProgressbar.postValue(false)
                    } catch (e: Throwable) {
                        loginError.postValue(e.message)
                        showProgressbar.postValue(false)
                    }
                }
            }
        }
        else
        {
            webServiceError.value = "No Internet Connection"
        }
    }

    private fun checkForEmptyFields() : Boolean {
        if(!username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()) return true
        return false
    }

}
package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.util.isConnected
import com.example.terasystemhrisv3.model.AccountDetails
import android.util.Patterns.EMAIL_ADDRESS
import com.example.terasystemhrisv3.service.RetrofitFactory
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.util.regex.Pattern

class UpdateViewModel(application: Application) : AndroidViewModel(application) {

    var webServiceError = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var userID = MutableLiveData<String>()
    var empID = MutableLiveData<String>()
    var firstName = MutableLiveData<String>()
    var firstNameErrorMsg = MutableLiveData<String>()
    var middleName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var lastNameErrorMsg = MutableLiveData<String>()
    var emailAddress = MutableLiveData<String>()
    var emailAddressErrorMsg = MutableLiveData<String>()
    var mobileNumber = MutableLiveData<String>()
    var mobileNumberErrorMsg = MutableLiveData<String>()
    var landline = MutableLiveData<String>()
    var landlineErrorMsg = MutableLiveData<String>()
    var showProgressbar = MutableLiveData<Boolean>()
    var isUpdateSuccessful = MutableLiveData<Boolean>()
    var isRequiredFieldEmpty = MutableLiveData<Boolean>()
    var isEmailValid = MutableLiveData<Boolean>()
    var isMobileNumberValid = MutableLiveData<Boolean>()
    var isLandlineValid = MutableLiveData<Boolean>()
    private val job = SupervisorJob()
    private val coroutineContext = Dispatchers.IO + job

    init {
        showProgressbar.value = false
        isUpdateSuccessful.value = false
        isRequiredFieldEmpty.value = false
        isEmailValid.value = false
        isMobileNumberValid.value = false
        isLandlineValid.value = false
    }

    fun addSpace(mobile: String?) {
        val countryCode: String
        val mobileInitials: String
        val secondSet: String
        val thirdSet: String
        if (mobile?.count() == 13) {
            countryCode = mobile.substring(0..2)
            mobileInitials = mobile.substring(3..5)
            secondSet = mobile.substring(6..8)
            thirdSet = mobile.substring(9..12)
            mobileNumber.value = "$countryCode $mobileInitials $secondSet $thirdSet"

        } else if (mobile?.count() == 11) {
            mobileInitials = mobile.substring(0..3)
            secondSet = mobile.substring(4..6)
            thirdSet = mobile.substring(7..10)
            mobileNumber.value = "$mobileInitials $secondSet $thirdSet"
        }
    }

    fun isEmailValid() {
        if(EMAIL_ADDRESS.matcher(emailAddress.value.toString()).matches())
        {
            isEmailValid.value = true
        }
        else
        {
            emailAddressErrorMsg.value = "Please enter a valid email"
        }
    }

    fun isMobileNumberValid() {
        if(Pattern.compile("^((\\+63) (9\\d{2}) \\d{3} \\d{4})|((09)\\d{2} \\d{3} \\d{4})\$").matcher(mobileNumber.value.toString()).matches())
        {
            isMobileNumberValid.value = true
        }
        else
        {
            mobileNumberErrorMsg.value = "Please enter a valid mobile number"
        }
    }

    fun isLandlineNumberValid() {
        if (landline.value.isNullOrEmpty() || Pattern.compile("^(\\+63) (2|(3[2-8])|(4[2-9])|(5[2-6])|(6[2-8])|(7[2-8])|(8[2-8])) \\d{3} \\d{4}|(02) \\d{3} \\d{4}|\\d{7}|(0)((3[2-8])|(4[2-9])|(5[2-6])|(6[2-8])|(7[2-8])|(8[2-8])) \\d{3} \\d{4}\$").matcher(landline.value.toString()).matches())
        {
            isLandlineValid.value = true
        }
        else
        {
            landlineErrorMsg.value = "Please enter a valid landline number"
        }
    }

    fun updateProfile(){
        if(isConnected(getApplication()))
        {
            if(isRequiredFieldEmpty.value == false && isEmailValid.value == true &&  isMobileNumberValid.value == true && isLandlineValid.value == true)
            {
                showProgressbar.value = true
                val service = RetrofitFactory.makeRetrofitService()
                CoroutineScope(coroutineContext).launch {
                    val response = service.Update(userID.value!!, firstName.value!!, middleName.value.toString(), lastName.value!!, emailAddress.value!!, mobileNumber.value?.replace(" ", "").toString(), landline.value?.replace(" ", "").toString())
                    withContext(Dispatchers.Main) {
                        try {
                            if (response.isSuccessful) {
                                val details = response.body()
                                if(details?.status == "0")
                                {
                                    isUpdateSuccessful.value = true
                                }
                                else
                                {
                                    webServiceError.value = response.body()?.message
                                    isUpdateSuccessful.value = false
                                }
                                showProgressbar.postValue(false)
                            } else {
                                webServiceError.postValue("Error: ${response.code()}")
                                showProgressbar.postValue(false)
                                isUpdateSuccessful.value = false
                            }
                        } catch (e: HttpException) {
                            webServiceError.postValue("Exception ${e.message}")
                            showProgressbar.postValue(false)
                            isUpdateSuccessful.value = false
                        } catch (e: Throwable) {
                            webServiceError.postValue(e.message)
                            showProgressbar.postValue(false)
                            isUpdateSuccessful.value = false
                        }
                    }
                }
            }
        }
        else
        {
            webServiceError.value = "No Internet Connection"
        }
    }

    fun saveNewProfile() {
        accountDetails.value!!.userID = accountDetails.value?.userID!!
        accountDetails.value!!.idNumber = accountDetails.value?.idNumber!!
        accountDetails.value!!.firstName = firstName.value.toString()
        accountDetails.value?.middleName = middleName.value
        accountDetails.value!!.lastName = lastName.value.toString()
        accountDetails.value!!.emailAddress = emailAddress.value.toString()
        accountDetails.value!!.mobileNumber = mobileNumber.value!!.replace(" ", "") // Causes mobile number upon update to be empty
        accountDetails.value?.landlineNumber = landline.value
    }

    fun checkForEmptyFields() {
        if (firstName.value.isNullOrEmpty()) {
            firstNameErrorMsg.value = "This field cannot be blank"
            isRequiredFieldEmpty.value = true
        }
        if (lastName.value.isNullOrEmpty()) {
            lastNameErrorMsg.value = "This field cannot be blank"
            isRequiredFieldEmpty.value = true
        }
        if (emailAddress.value.isNullOrEmpty()) {
            emailAddressErrorMsg.value = "This field cannot be blank"
            isRequiredFieldEmpty.value = true
        }
        if (mobileNumber.value.isNullOrEmpty()) {
            mobileNumberErrorMsg.value = "This field cannot be blank"
            isRequiredFieldEmpty.value = true
        }
    }
}
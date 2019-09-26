package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.util.isFieldNullOrEmpty
import com.example.terasystemhrisv3.model.AccountDetails
import java.util.*

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    var accountDetails = MutableLiveData<AccountDetails>()
    var profileName = MutableLiveData<String>()
    var userInitials = MutableLiveData<String>()
    var maskedEmail = MutableLiveData<String>()
    var maskedMobile = MutableLiveData<String>()

    init {

    }

    fun getInitials(){
        val firstNameInitial = accountDetails.value?.firstName?.get(0)?.toString()?.toUpperCase(Locale.ENGLISH)
        val lastNameInitial = accountDetails.value?.lastName?.get(0)?.toString()?.toUpperCase(Locale.ENGLISH)
        userInitials.value = "$firstNameInitial$lastNameInitial"
    }

    fun getProfileName(){
        if(!isFieldNullOrEmpty(accountDetails.value?.middleName!!))
        {
            profileName.value = ("${accountDetails.value?.firstName} ${accountDetails.value?.middleName} ${accountDetails.value?.lastName}").toUpperCase(Locale.ENGLISH)
        }
        else
        {
            profileName.value = ("${accountDetails.value?.firstName} ${accountDetails.value?.lastName}").toUpperCase(Locale.ENGLISH)
        }
    }

    fun maskEmail(){
        val userEmail: String? = accountDetails.value?.emailAddress
        val userEmailPrefix: String = accountDetails.value?.emailAddress!!.substringBeforeLast("@")
        val visibleCharInEmail = userEmail?.substring(0..3)
        val hiddenCharInEmail = userEmail!!.replaceBefore('@', "*****")
        if(userEmailPrefix.count() <= 3)
        {
            maskedEmail.value = "$userEmailPrefix$hiddenCharInEmail"
        }
        else if(userEmailPrefix.count() > 3)
        {
            maskedEmail.value = "$visibleCharInEmail$hiddenCharInEmail"
        }
    }

    fun maskMobileNumber(){
        val mobileNumber = accountDetails.value?.mobileNumber
        val countryCode: String
        val mobilePrefix: String
        val mobileSuffix: String
        if(mobileNumber?.count() == 13)
        {
            countryCode = mobileNumber.substring(0..2)
            mobilePrefix = mobileNumber.substring(3..5)
            mobileSuffix = mobileNumber.substring(9..12)
            maskedMobile.value = "$countryCode $mobilePrefix *** $mobileSuffix"
        }
        else if(mobileNumber?.count() == 11)
        {
            mobilePrefix = mobileNumber.substring(0..3)
            mobileSuffix = mobileNumber.substring(7..10)
            maskedMobile.value = "$mobilePrefix *** $mobileSuffix"
        }
    }

}
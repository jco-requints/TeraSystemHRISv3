package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import com.example.terasystemhrisv3.helper.BottomNavigationPosition
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import org.json.JSONObject


class FragmentActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val KEY_POSITION = "keyPosition"
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.LOGS
    var webServiceError = MutableLiveData<String>()
    var fragmentActivityError = MutableLiveData<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var showProgressbar = MutableLiveData<Boolean>()
    var itemSelected = MutableLiveData<Int>()
    var showAlert = MutableLiveData<Boolean>()
    var logOutAlertDialogMessage = MutableLiveData<String>()
    var logOutAlertDialogResponse = MutableLiveData<Boolean>()

    init {
        showProgressbar.value = false
    }

    fun initFragment(newAccountDetails: AccountDetails) {
        accountDetails.value = newAccountDetails
        itemSelected.value = navPosition.position
    }

    fun logOutDialog(context: Context, error: String) {
        val dialog = AlertDialog.Builder(context)
        var response: Boolean
        dialog.setTitle(error)
        dialog.setCancelable(false)
        dialog.setPositiveButton("YES",
                DialogInterface.OnClickListener { dialog, which ->
                    logOutAlertDialogResponse.value = true
                })
        dialog.setNegativeButton("CANCEL",
                DialogInterface.OnClickListener { dialog, which ->
                    logOutAlertDialogResponse.value = false
                })
        val alertDialog = dialog.create()
        alertDialog.show()
    }

}
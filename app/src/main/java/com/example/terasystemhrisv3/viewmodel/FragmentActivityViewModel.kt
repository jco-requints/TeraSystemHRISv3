package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.terasystemhrisv3.URLs
import com.example.terasystemhrisv3.extension.active
import com.example.terasystemhrisv3.extension.attach
import com.example.terasystemhrisv3.extension.detach
import com.example.terasystemhrisv3.helper.BottomNavigationPosition
import com.example.terasystemhrisv3.helper.createFragment
import com.example.terasystemhrisv3.helper.findNavigationPositionById
import com.example.terasystemhrisv3.helper.getTag
import com.example.terasystemhrisv3.isConnected
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.services.WebServiceConnection
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import org.json.JSONObject
import java.net.URLEncoder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.terasystemhrisv3.ui.LoginActivity


class FragmentActivityViewModel(application: Application) : AndroidViewModel(application), NetworkRequestInterface {

    private val KEY_POSITION = "keyPosition"
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.LOGS
    var webServiceError = MutableLiveData<String>()
    var logsError = MutableLiveData<String>()
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

            }
            else
            {
                logsError.value = jsonObject.get("message").toString()
            }
        } catch (ex: Exception){
            webServiceError.value = result
        }

    }

}
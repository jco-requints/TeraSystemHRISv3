package com.example.terasystemhrisv3.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.util.afterTextChanged
import com.example.terasystemhrisv3.util.alertDialog
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.viewmodel.UpdateViewModel
import kotlinx.android.synthetic.main.activity_update.*


class Update : AppCompatActivity() {

    private lateinit var updateViewModel: UpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        setSupportActionBar(findViewById(R.id.activity_update_toolbar))
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        updateViewModel = ViewModelProviders.of(this).get(UpdateViewModel::class.java)
        val data = intent.extras
        val accountDetails = data?.getParcelable<AccountDetails>("keyAccountDetails")!!
        updateViewModel.accountDetails.value = accountDetails

        close_button.setOnClickListener {
            finish()
        }

        updateViewModel.accountDetails.observe(this, Observer { userDetails ->
            updateViewModel.userID.value = userDetails.username
            profile_id_edit.setText(userDetails.empID)
            updateViewModel.empID.value = userDetails.empID
            profile_id_edit.isEnabled = false
            firstname_edit.setText(userDetails.firstName)
            updateViewModel.firstName.value = userDetails.firstName
            middlename_edit.setText(userDetails.middleName)
            updateViewModel.middleName.value = userDetails.middleName
            lastname_edit.setText(userDetails.lastName)
            updateViewModel.lastName.value = userDetails.lastName
            email_edit.setText(userDetails.emailAddress)
            updateViewModel.emailAddress.value = userDetails.emailAddress
            updateViewModel.addSpace(userDetails.mobileNumber)
            mobile_edit.setText(updateViewModel.mobileNumber.value)
            landline_edit.setText(userDetails.landlineNumber)
        })

        firstname_edit.afterTextChanged {
            updateViewModel.firstName.value = it
        }

        middlename_edit.afterTextChanged {
            updateViewModel.middleName.value = it
        }

        lastname_edit.afterTextChanged {
            updateViewModel.lastName.value = it
        }

        email_edit.afterTextChanged {
            updateViewModel.emailAddress.value = it
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            updateViewModel.mobileNumber.value = mobile_edit.addTextChangedListener(PhoneNumberFormattingTextWatcher()).toString()
        }

        mobile_edit.afterTextChanged {
            updateViewModel.mobileNumber.value = it
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            updateViewModel.landline.value = landline_edit.addTextChangedListener(PhoneNumberFormattingTextWatcher()).toString()
        }

        landline_edit.afterTextChanged {
            updateViewModel.landline.value = it
        }

        update_profile_button.setOnClickListener {
            updateViewModel.checkForEmptyFields()
            updateViewModel.isEmailValid()
            updateViewModel.isMobileNumberValid()
            updateViewModel.isLandlineNumberValid()
            updateViewModel.updateProfile()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        updateViewModel.firstNameErrorMsg.observe(this, Observer {
            firstname_edit.error = it
        })

        updateViewModel.lastNameErrorMsg.observe(this, Observer {
            lastname_edit.error = it
        })

        updateViewModel.emailAddressErrorMsg.observe(this, Observer {
            email_edit.error = it
        })

        updateViewModel.mobileNumberErrorMsg.observe(this, Observer {
            mobile_edit.error = it
        })

        updateViewModel.landlineErrorMsg.observe(this, Observer {
            landline_edit.error = it
        })

        updateViewModel.showProgressbar.observe(this, Observer {
            updateProgressBarHolder.visibility = if (it) View.VISIBLE
            else View.GONE
        })

        updateViewModel.webServiceError.observe(this, Observer {
            alertDialog(this, it)
        })

        updateViewModel.isUpdateSuccessful.observe(this, Observer {
            if(it)
            {
                updateViewModel.saveNewProfile()
                val intent = Intent(this@Update, Success::class.java).apply {
                    this.putExtra("keyAccountDetails", updateViewModel.accountDetails.value)
                }
                startActivity(intent)
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onBackPressed()
            finish()
            true
        } else super.onKeyDown(keyCode, event)
    }
}
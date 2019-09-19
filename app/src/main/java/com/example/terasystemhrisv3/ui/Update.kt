package com.example.terasystemhrisv3.ui
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.services.NetworkRequestInterface
import com.example.terasystemhrisv3.services.WebServiceConnection
import kotlinx.android.synthetic.main.activity_update.*
import org.json.JSONObject
import java.net.URL
import java.util.regex.Pattern


class Update : AppCompatActivity(), NetworkRequestInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        val mURL = URL("http://222.222.222.71:9080/MobileAppTraining/AppTrainingUpdateProfile.htm").toString()
        setSupportActionBar(findViewById(R.id.activity_update_toolbar))
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        val data = intent.extras
        val accountDetails = data?.getParcelable<AccountDetails>("keyAccountDetails")!!
        val username_string = accountDetails.username
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            landline_edit.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        }

        profile_id_edit.setText(accountDetails.empID)
        firstname_edit.setText(accountDetails.firstName)
        middlename_edit.setText(accountDetails.middleName)
        lastname_edit.setText(accountDetails.lastName)
        email_edit.setText(accountDetails.emailAddress)
        addSpace(accountDetails.mobileNumber)
        landline_edit.setText(accountDetails.landlineNumber)
        profile_id_edit.isEnabled = false

        mobile_edit.addTextChangedListener(object : TextWatcher {

            var ignoreChange = false

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!ignoreChange) {
                    var string = s.toString()
                    val mobile_prefix_one_firstCase = "^(09)\\d{2}\$".toRegex()
                    val mobile_prefix_one_secondCase = "^(\\+63)\$".toRegex()
                    val mobile_prefix_two_firstCase = "^(09)\\d{2} \\d{3}\$".toRegex()
                    val mobile_prefix_two_secondCase = "^(\\+63) \\d{3}\$".toRegex()
                    val mobile_prefix_three_secondCase = "^(\\+63) \\d{3} \\d{3}\$".toRegex()
                    if (string.matches(mobile_prefix_one_firstCase)) {
                        string += " "
                    } else if (string.matches(mobile_prefix_one_secondCase)) {
                        string += " "
                    }
                    if (string.matches(mobile_prefix_two_firstCase)) {
                        string += " "
                    } else if (string.matches(mobile_prefix_two_secondCase)) {
                        string += " "
                    }
                    if (string.matches(mobile_prefix_three_secondCase)) {
                        string += " "
                    }
                    ignoreChange = true
                    mobile_edit.setText(string)
                    mobile_edit.setSelection(mobile_edit.text.length)
                    ignoreChange = false
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val string = s.toString()
                val mobile_prefix_one_firstCase = "^(09)\\d{2}\$".toRegex()
                val mobile_prefix_one_secondCase = "^(\\+63)\$".toRegex()
                val mobile_prefix_two_firstCase = "^(09)\\d{2} \\d{3}\$".toRegex()
                val mobile_prefix_two_secondCase = "^(\\+63) \\d{3}\$".toRegex()
                val mobile_prefix_three_secondCase = "^(\\+63) \\d{3} \\d{3}\$".toRegex()
                ignoreChange =
                    string.matches(mobile_prefix_one_firstCase) || string.matches(mobile_prefix_one_secondCase)
                            || string.matches(mobile_prefix_two_firstCase) || string.matches(
                        mobile_prefix_two_secondCase
                    )
                            || string.matches(mobile_prefix_three_secondCase)
            }
        })

        update_profile_button.setOnClickListener {
            val fieldsAreEmpty = checkForEmptyFields()

            val isNumberValid = isMobileNumberValid(mobile_edit.text.toString())

            val isEmailValid = isEmailValid(email_edit.text.toString())

            val isLandlineNumberValid = isLandlineNumberValid(landline_edit.text.toString())

            if (!isEmailValid && email_edit.text.trim().toString() != "") {
                email_edit.error = "Please enter a valid email"
            }

            if (!isNumberValid && mobile_edit.text.trim().toString() != "") {
                mobile_edit.error = "Please enter a valid mobile number"
            }

            if (!isLandlineNumberValid) {
                landline_edit.error = "Please enter a valid landline number"
            }

            if (!fieldsAreEmpty && isEmailValid && isNumberValid && isLandlineNumberValid) {
                if (isConnected(this)) {
                    WebServiceConnection(this).execute(
                        mURL, username_string, firstname_edit.text.toString(),
                        middlename_edit.text.toString(), lastname_edit.text.toString(),
                        email_edit.text.toString(), mobile_edit.text.toString().replace(" ", ""),
                        landline_edit.text.toString().replace(" ", "")
                    )
                } else {
                    updatePopupHolder.visibility = View.VISIBLE
                    update_network_status.text = getString(R.string.no_internet_message)
                }
            }
        }

        close_button.setOnClickListener {
            finish()
        }

        updateTxtClose.setOnClickListener {
            updatePopupHolder.visibility = View.GONE
        }
    }

    override fun beforeNetworkCall() {
        updateProgressBarHolder.visibility = View.VISIBLE
    }

    override fun afterNetworkCall(result: String?) {
        updateProgressBarHolder.visibility = View.GONE
        val data = intent.extras
        val accountDetails = data?.getParcelable<AccountDetails>("keyAccountDetails")!!
        val username_string = accountDetails.username
        if (result == "Connection Timeout") {
            updatePopupHolder.visibility = View.VISIBLE
            update_network_status.text = getString(R.string.connection_timeout_message)
        } else if (result.isNullOrEmpty()) {
            updatePopupHolder.visibility = View.VISIBLE
            update_network_status.text = getString(R.string.server_error)
        } else {
            val jsonObject = JSONObject(result)
            val status = jsonObject?.get("status").toString()
            if (status == "0") {
                saveNewProfile(
                    username_string,
                    profile_id_edit.text.toString(),
                    firstname_edit.text.toString(),
                    middlename_edit.text.toString(),
                    lastname_edit.text.toString(),
                    email_edit.text.toString(),
                    mobile_edit.text.toString().replace(" ", ""),
                    landline_edit.text.toString(),
                    accountDetails
                )
            } else {
                updatePopupHolder.visibility = View.VISIBLE
                update_network_status.text = getString(R.string.server_error)
            }
        }
    }

    private fun checkForEmptyFields(): Boolean {
        var isEmpty = false
        if (firstname_edit.text.trim().toString() == "") {
            firstname_edit.error = "This field cannot be blank"
            isEmpty = true
        }
        if (lastname_edit.text.trim().toString() == "") {
            lastname_edit.error = "This field cannot be blank"
            isEmpty = true
        }
        if (email_edit.text.trim().toString() == "") {
            email_edit.error = "This field cannot be blank"
            isEmpty = true
        }
        if (mobile_edit.text.trim().toString() == "") {
            mobile_edit.error = "This field cannot be blank"
            isEmpty = true
        }
        return isEmpty
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isMobileNumberValid(mobile_number: String): Boolean {
        return Pattern.compile(
//                "^(09|\\+639)\\d{9}\$"
            "^((\\+63) (9\\d{2}) \\d{3} \\d{4})|((09)\\d{2} \\d{3} \\d{4})\$"
        ).matcher(mobile_number).matches()
    }

    fun isLandlineNumberValid(landline_number: String): Boolean {
        if (landline_number.isNullOrEmpty()) {
            return true
        }
        return Pattern.compile(
            "^(\\+63) (2|(3[2-8])|(4[2-9])|(5[2-6])|(6[2-8])|(7[2-8])|(8[2-8])) \\d{3} \\d{4}|(02) \\d{3} \\d{4}|\\d{7}|(0)((3[2-8])|(4[2-9])|(5[2-6])|(6[2-8])|(7[2-8])|(8[2-8])) \\d{3} \\d{4}\$"
        ).matcher(landline_number).matches()
    }

    fun addSpace(mobile: String?) {
        val country_code: String
        val mobile_initials: String
        val second_set: String
        val third_set: String
        var formatted_number: String = ""
        if (mobile?.count() == 13) {
            country_code = mobile.substring(0..2)
            mobile_initials = mobile.substring(3..5)
            second_set = mobile.substring(6..8)
            third_set = mobile.substring(9..12)
            formatted_number = "$country_code $mobile_initials $second_set $third_set"
            mobile_edit.setText(formatted_number)

        } else if (mobile?.count() == 11) {
            mobile_initials = mobile.substring(0..3)
            second_set = mobile.substring(4..6)
            third_set = mobile.substring(7..10)
            formatted_number = "$mobile_initials $second_set $third_set"
            mobile_edit.setText(formatted_number)
        } else {
            mobile_edit.setText(mobile)
        }
    }

    private fun saveNewProfile(
        username_string: String,
        empID_string: String,
        firstname_string: String,
        middlename_string: String?,
        lastname_string: String,
        email_string: String,
        mobile_string: String,
        landline_string: String?,
        accountDetails: AccountDetails
    ) {
        accountDetails.username = username_string
        accountDetails.empID = empID_string
        accountDetails.firstName = firstname_string
        accountDetails.middleName = middlename_string
        accountDetails.lastName = lastname_string
        accountDetails.emailAddress = email_string
        accountDetails.mobileNumber = mobile_string
        accountDetails.landlineNumber = landline_string
        val intent = Intent(this@Update, Success::class.java).apply {
            this.putExtra("keyAccountDetails", accountDetails)
        }
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onBackPressed()
            finish()
            true
        } else super.onKeyDown(keyCode, event)
    }

    fun isConnected(context: Context): Boolean {
        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info)
                    if (i.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
        }
        return false
    }
}

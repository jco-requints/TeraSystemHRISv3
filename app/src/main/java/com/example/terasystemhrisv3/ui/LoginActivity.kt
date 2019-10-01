package com.example.terasystemhrisv3.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.service.RetrofitFactory
import com.example.terasystemhrisv3.util.afterTextChanged
import com.example.terasystemhrisv3.util.alertDialog
import com.example.terasystemhrisv3.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        userId_edit.afterTextChanged {
            loginViewModel.username.value = it
        }

        password_edit.afterTextChanged {
            loginViewModel.password.value = it
        }

        loginViewModel.areFieldsEmpty.observe(this, Observer {
                login_button.isEnabled = it
        })

        login_button.setOnClickListener{
            userId_edit.clearFocus()
            password_edit.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            loginViewModel.login()
        }

        loginViewModel.showProgressbar.observe(this, Observer {
            progressBarHolder?.visibility = if (it) View.VISIBLE
            else View.GONE
        })

        loginViewModel.accountDetails.observe(this, Observer {
            it?.let {
                    val intent = Intent(this@LoginActivity,
                    FragmentActivity::class.java).apply {
                    this.putExtra("keyAccountDetails", it)
                }
                startActivity(intent)
            }
        })

        loginViewModel.webServiceError.observe(this, Observer {
            alertDialog(this, it)
        })

        loginViewModel.loginError.observe(this, Observer {
            val toast = Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT)
            toast.show()
        })
    }
}

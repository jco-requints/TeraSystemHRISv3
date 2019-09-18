package com.example.terasystemhrisv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.terasystemhrisv3.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

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
           loginViewModel.login()
        }

        loginViewModel.progressbar.observe(this, Observer {
            progressBarHolder?.visibility = if (it) View.VISIBLE
            else View.GONE
        })

        loginViewModel.error.observe(this, Observer {
            if (it.isNotEmpty()){
                alertDialog(this, it.toString())
            }
        })
    }
}

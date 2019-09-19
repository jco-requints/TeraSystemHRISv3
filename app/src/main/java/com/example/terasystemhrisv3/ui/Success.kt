package com.example.terasystemhrisv3.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails
import kotlinx.android.synthetic.main.activity_success.*


class Success : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        setSupportActionBar(findViewById(R.id.activity_success_toolbar))
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(false)
        val data = intent.extras
        val accountDetails = data?.getParcelable<AccountDetails>("keyAccountDetails")!!

        ok_button.setOnClickListener {
            saveNewProfile(accountDetails)
        }
    }

    private fun saveNewProfile(accountDetails: AccountDetails)
    {
        val intent = Intent(this@Success, FragmentActivity ::class.java).apply {
            this.putExtra("keyAccountDetails", accountDetails)
            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            val data = intent.extras
            val accountDetails = data?.getParcelable<AccountDetails>("keyAccountDetails")!!
            saveNewProfile(accountDetails)
            true
        } else super.onKeyDown(keyCode, event)
    }
}

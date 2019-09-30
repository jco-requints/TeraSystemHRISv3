package com.example.terasystemhrisv3.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_main.*
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.terasystemhrisv3.interfaces.AppBarController
import com.example.terasystemhrisv3.interfaces.FragmentNavigator
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.extension.active
import com.example.terasystemhrisv3.extension.attach
import com.example.terasystemhrisv3.extension.detach
import com.example.terasystemhrisv3.helper.BottomNavigationPosition
import com.example.terasystemhrisv3.helper.createFragment
import com.example.terasystemhrisv3.helper.findNavigationPositionById
import com.example.terasystemhrisv3.helper.getTag
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.viewmodel.FragmentActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentActivity : AppCompatActivity(), AppBarController, FragmentNavigator {

    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.LOGS
    private lateinit var fragmentActivityViewModel: FragmentActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)
        fragmentActivityViewModel = ViewModelProviders.of(this).get(FragmentActivityViewModel::class.java)
        this.title = null
        var itemSelected = 0
        val data = intent.extras
        val accountDetails = data?.getParcelable<AccountDetails>("keyAccountDetails")!!
        val bundle = Bundle()

        findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
        }

        fragmentActivityViewModel.initFragment(accountDetails)

        fragmentActivityViewModel.accountDetails.observe(this, Observer {
            bundle.putParcelable("keyAccountDetails", it)
        })

        fragmentActivityViewModel.itemSelected.observe(this, Observer {
            itemSelected = it
        })

        fragmentActivityViewModel.showAlert.observe(this, Observer {
            if(it){
                fragmentActivityViewModel.logOutDialog(this, "Are you sre you want to logout?")
            }
        })

        fragmentActivityViewModel.logOutAlertDialogResponse.observe(this, Observer {
            if(it){
                val intent = Intent(this, LoginActivity::class.java).apply {
                    this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
        })

        findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
            // This is required in Support Library 27 or lower:
            // bottomNavigation.disableShiftMode()
            active(itemSelected) // Extension function
            setOnNavigationItemSelectedListener { item ->
                navPosition = findNavigationPositionById(item.itemId)
                switchFragment(navPosition)
            }
        }

        initFragment(savedInstanceState)
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState ?: switchFragment(BottomNavigationPosition.LOGS)
    }

    private fun switchFragment(navPosition: BottomNavigationPosition): Boolean {
        return findFragment(navPosition).let {
            if (it.isAdded) return false
            supportFragmentManager.detach() // Extension function
            supportFragmentManager.attach(it, navPosition.getTag()) // Extension function
            supportFragmentManager.executePendingTransactions()
        }
    }

    private fun findFragment(position: BottomNavigationPosition): Fragment {
        return supportFragmentManager.findFragmentByTag(position.getTag()) ?: position.createFragment(fragmentActivityViewModel.accountDetails.value!!)
    }

    override fun onBackPressed() {
        val data = intent.extras
        val accountDetails = data?.getParcelable<AccountDetails>("keyAccountDetails")!!
        val bundle = Bundle()

        when (supportFragmentManager.findFragmentById(container.id)) {
            is LogsFragment -> fragmentActivityViewModel.showAlert.value = true

            is AddTimeLogFragment,
            is AddTimeLogSuccessFragment,
            is FileLeaveFragment,
            is FileLeaveSuccessFragment -> super.onBackPressed()

            else -> findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
                active(0) // Extension function
                val fragmentManager = supportFragmentManager
                val fragment = LogsFragment()
                bundle.putParcelable("keyAccountDetails", accountDetails)
                fragment.arguments = bundle
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.container, fragment)
                fragmentTransaction.commit()
            }
        }
    }

    private fun replaceFragment(mBundle: Bundle, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragment.arguments = mBundle
        val fragmentTransaction = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun setTitle(title: String) {
        title_toolbar.text = title
    }

    override fun setCancelButtonTitle(cancelTitle: String?) {
        backBtn.text = cancelTitle
    }

    override fun setAddButtonTitle(addTitle: String?) {
        toolbar_button.text = addTitle
    }

    override fun getToolBar(): Toolbar {
        return toolbar
    }

    override fun getCancelButton(): Button {
        return backBtn
    }

    override fun getAddButton(): Button {
        return toolbar_button
    }

    override fun getSupportFragmentManager(): FragmentManager {
        return super.getSupportFragmentManager()
    }

    override fun showAddTimeLog(mBundle: Bundle, fragment: Fragment) {
        replaceFragment(mBundle, fragment)
    }

    override fun showAddTimeLogSuccess(mBundle: Bundle, fragment: Fragment) {
        replaceFragment(mBundle, fragment)
    }

    override fun showFileLeave(mBundle: Bundle, fragment: Fragment) {
        replaceFragment(mBundle, fragment)
    }

    override fun showFileLeaveSuccess(mBundle: Bundle, fragment: Fragment) {
        replaceFragment(mBundle, fragment)
    }

    override fun showTimeLogs(mBundle: Bundle, fragment: Fragment) {
        replaceFragment(mBundle, fragment)
    }

    override fun showLogDetails(mBundle: Bundle, fragment: Fragment) {
        replaceFragment(mBundle, fragment)
    }

}
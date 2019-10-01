package com.example.terasystemhrisv3.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.terasystemhrisv3.interfaces.AppBarController
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private var myInterface: AppBarController? = null
    private lateinit var profileViewModel: ProfileViewModel
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        if (bundle != null)
        {
            myDetails = bundle.getParcelable("keyAccountDetails")!!
        }
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        myInterface?.setTitle(getString(R.string.profile_title))
        myInterface?.setAddButtonTitle(getString(R.string.title_activity_logout))
        myInterface?.setCancelButtonTitle(null)
        myInterface?.getAddButton()?.setBackgroundResource(0)
        myInterface?.getAddButton()?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        myInterface?.getAddButton()?.visibility = View.VISIBLE
        myInterface?.getCancelButton()?.visibility = View.GONE

        // logic for Logout Button
        myInterface?.getAddButton()?.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java).apply {
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            (activity as Activity).overridePendingTransition(0, 0)
        }

        profileViewModel.accountDetails.value = myDetails

        view.profile_id?.text = profileViewModel.accountDetails.value!!.idNumber

        profileViewModel.accountDetails.observe(viewLifecycleOwner, Observer {
            profileViewModel.getInitials()
            profileViewModel.getProfileName()
            profileViewModel.maskEmail()
            profileViewModel.maskMobileNumber()
        })

        profileViewModel.userInitials.observe(viewLifecycleOwner, Observer {
            view.initials?.text = it
        })

        profileViewModel.profileName.observe(viewLifecycleOwner, Observer {
            view.name_text?.text = it
        })

        profileViewModel.maskedEmail.observe(viewLifecycleOwner, Observer {
            view.profile_email?.text = it
        })

        profileViewModel.maskedMobile.observe(viewLifecycleOwner, Observer {
            view.profile_number?.text = it
        })

        view.update_button.setOnClickListener {
            val intent = Intent(activity, Update::class.java).apply {
                this.putExtra("keyAccountDetails", myDetails)
            }
            startActivity(intent)
            (activity as Activity).overridePendingTransition(0, 0)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is AppBarController)
        {
            myInterface = context
        }
    }

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
        fun newInstance(accountDetails: AccountDetails) = ProfileFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable("keyAccountDetails", accountDetails)
            this.arguments = bundle
        }
    }

}
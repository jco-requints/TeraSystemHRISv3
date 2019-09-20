package com.example.terasystemhrisv3.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.terasystemhrisv3.AppBarController
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails

class ClientsFragment : Fragment() {

    private var myInterface: AppBarController? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clients, container, false)
        myInterface?.setTitle(getString(R.string.clients_title))
        myInterface?.setAddButtonTitle(null)
        myInterface?.setCancelButtonTitle(null)
        myInterface?.getCancelButton()?.visibility = View.GONE
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context is AppBarController)
        {
            myInterface = context
        }
    }

    companion object {
        val TAG: String = ClientsFragment::class.java.simpleName
        fun newInstance(accountDetails: AccountDetails) = ClientsFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable("keyAccountDetails", accountDetails)
            this.arguments = bundle
        }
    }

}
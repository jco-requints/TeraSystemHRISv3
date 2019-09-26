package com.example.terasystemhrisv3.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.text.format.DateFormat
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.example.terasystemhrisv3.interfaces.AppBarController
import com.example.terasystemhrisv3.interfaces.FragmentNavigator
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.util.alertDialog
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.viewmodel.AddTimeLogViewModel
import kotlinx.android.synthetic.main.fragment_addtimelog.view.*
import java.util.*

class AddTimeLogFragment : Fragment() {

    private var myInterface: AppBarController? = null
    private var fragmentNavigatorInterface: FragmentNavigator? = null
    private lateinit var addTimeLogViewModel: AddTimeLogViewModel
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        addTimeLogViewModel = ViewModelProviders.of(this).get(AddTimeLogViewModel::class.java)
        if (bundle != null)
        {
            myDetails = bundle.getParcelable("keyAccountDetails")!!
        }
        addTimeLogViewModel.accountDetails.value = myDetails
        val view = inflater.inflate(R.layout.fragment_addtimelog, container, false)
        myInterface?.setTitle(getString(R.string.addtimelog_title))
        myInterface?.setAddButtonTitle(getString(R.string.done_title))
        myInterface?.setCancelButtonTitle(getString(R.string.cancel_title))
        myInterface?.getAddButton()?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        myInterface?.getCancelButton()?.visibility = View.VISIBLE
        myInterface?.getCancelButton()?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F)
        val adapter = ArrayAdapter.createFromResource(view.context,
            R.array.log_type, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.spinner?.adapter = adapter

        view.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedText: TextView = parent?.getChildAt(0) as TextView
                selectedText.setTextColor(Color.BLACK)
            }
        }

        myInterface?.getCancelButton()?.setOnClickListener {
            val fragmentManager = myInterface?.getSupportFragmentManager()
            fragmentManager?.popBackStackImmediate()
        }

        myInterface?.getAddButton()?.setOnClickListener {
            addTimeLogViewModel.selectedItem.value = view.spinner.selectedItemPosition + 1
            addTimeLogViewModel.addTimeLog()
        }

        addTimeLogViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            view.progressBarHolder.visibility = if (it) View.VISIBLE
            else View.GONE
        })

        addTimeLogViewModel.webServiceError.observe(viewLifecycleOwner, Observer { message ->
            this.context?.let { mContext -> alertDialog(mContext, message) }
        })

        addTimeLogViewModel.isAddTimeLogSuccesful.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                val mBundle = Bundle()
                val current = Calendar.getInstance().time
                val currentTime = DateFormat.format("h:mm a", current).toString()
                mBundle.putInt("logType", addTimeLogViewModel.selectedItem.value!!)
                mBundle.putString("currentTime", currentTime)
                mBundle.putParcelable("keyAccountDetails", addTimeLogViewModel.accountDetails.value)
                fragmentNavigatorInterface?.showAddTimeLogSuccess(mBundle, AddTimeLogSuccessFragment())
            }
        })

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is AppBarController)
        {
            myInterface = context
        }
        if(context is FragmentNavigator)
        {
            fragmentNavigatorInterface = context
        }
    }

    companion object {
        val TAG: String = AddTimeLogFragment::class.java.simpleName
        fun newInstance(bundle: Bundle) = AddTimeLogFragment().apply {
            this.arguments = bundle
        }
    }

}
package com.example.terasystemhrisv3.ui

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.example.terasystemhrisv3.interfaces.AppBarController
import com.example.terasystemhrisv3.interfaces.FragmentNavigator
import com.example.terasystemhrisv3.R
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.util.alertDialog
import com.example.terasystemhrisv3.viewmodel.FileLeaveViewModel
import kotlinx.android.synthetic.main.fragment_fileleave.view.spinner
import kotlinx.android.synthetic.main.fragment_fileleave.view.*
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class FileLeaveFragment : Fragment() {
    private var myInterface: AppBarController? = null
    private var fragmentNavigatorInterface: FragmentNavigator? = null
    private lateinit var fileLeaveViewModel: FileLeaveViewModel
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")
    private lateinit var selectedTypeofLeave: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        fileLeaveViewModel = ViewModelProviders.of(this).get(FileLeaveViewModel::class.java)
        if (bundle != null)
        {
            myDetails = bundle.getParcelable("keyAccountDetails")!!
        }
        fileLeaveViewModel.accountDetails.value = myDetails
        val view = inflater.inflate(R.layout.fragment_fileleave, container, false)
        myInterface?.setTitle(getString(R.string.fileleave_title))
        myInterface?.setAddButtonTitle(getString(R.string.done_title))
        myInterface?.setCancelButtonTitle(getString(R.string.cancel_title))
        myInterface?.getAddButton()?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        myInterface?.getCancelButton()?.visibility = View.VISIBLE
        myInterface?.getCancelButton()?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F)

        myInterface?.getCancelButton()?.setOnClickListener {
            val fragmentManager = myInterface?.getSupportFragmentManager()
            fragmentManager?.popBackStackImmediate()
        }

        //code to get current date
        val current = Calendar.getInstance().time
        val currentDate = DateFormat.format("MMMM d, yyyy", current).toString()

        //code for Leave toggle
        selectedTypeofLeave = "1"
        view.vlToggle.setBackgroundColor(Color.parseColor("#1D8ECE"))
        view.vlToggle.setTextColor(Color.parseColor("#FFFFFF"))

        view.vlToggle.setOnClickListener {
            view.vlToggle.setBackgroundColor(Color.parseColor("#1D8ECE"))
            view.vlToggle.setTextColor(Color.parseColor("#FFFFFF"))

            view.slToggle.setBackgroundResource(R.drawable.toggleborder)
            view.slToggle.setTextColor(Color.parseColor("#FF858484"))
            selectedTypeofLeave = "1"
        }

        view.slToggle.setOnClickListener {
            view.slToggle.setBackgroundColor(Color.parseColor("#1D8ECE"))
            view.slToggle.setTextColor(Color.parseColor("#FFFFFF"))

            view.vlToggle.setBackgroundResource(R.drawable.toggleborder)
            view.vlToggle.setTextColor(Color.parseColor("#FF858484"))
            selectedTypeofLeave = "2"
        }


        //Code for spinner
        val adapter = ArrayAdapter.createFromResource(view.context,
            R.array.time_type, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.spinner?.adapter = adapter

        //code for default value of start and end date
        view.startDate.text = currentDate
        view.endDate.text = currentDate

        //code to hide or show end date
        view.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                val selectedText: TextView = parent?.getChildAt(0) as TextView
                selectedText.setTextColor(Color.BLACK)

                if (selectedItem == "Whole Day")
                {
                    getView()?.endDateHolder?.visibility = View.VISIBLE
                    getView()?.endDate?.text = currentDate
                    getView()?.startDateSuccessTitle?.text = getString(R.string.start_date_title)
                }
                else
                {
                    getView()?.endDateHolder?.visibility = View.GONE
                    getView()?.endDate?.text = null
                    getView()?.startDateSuccessTitle?.text = getString(R.string.file_leave_date)
                }
            }

        }

        //code for Date Picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val wordMonths= arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

        view.startDate?.setOnClickListener {
            val dpd = DatePickerDialog(container!!.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                val wordMonth = wordMonths[monthOfYear]
                val date = "$wordMonth $dayOfMonth, $year"
                this.view?.startDate?.text = date
            }, year, month, day)
            dpd.datePicker.minDate = System.currentTimeMillis()
            dpd.show()
        }

        view.endDate?.setOnClickListener {
            val dpd = DatePickerDialog(container!!.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                val wordMonth = wordMonths[monthOfYear]
                val date = "$wordMonth $dayOfMonth, $year"
                this.view?.endDate?.text = date
            }, year, month, day)
            dpd.datePicker.minDate = System.currentTimeMillis()
            dpd.show()
        }

        myInterface?.getAddButton()?.setOnClickListener {
            fileLeaveViewModel.selectedTypeOfLeave.value = selectedTypeofLeave
            fileLeaveViewModel.startDate.value = view?.startDate?.text.toString()
            fileLeaveViewModel.endDate.value = view?.endDate?.text.toString()
            fileLeaveViewModel.selectedItem.value = view.spinner.selectedItemPosition + 1
            fileLeaveViewModel.fileLeave()
        }

        fileLeaveViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            view.progressBarHolder.visibility = if (it) View.VISIBLE
            else View.GONE
        })

        fileLeaveViewModel.webServiceError.observe(viewLifecycleOwner, Observer { message ->
            this.context?.let { mContext -> alertDialog(mContext, message) }
        })

        fileLeaveViewModel.isFileLeaveSuccesful.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                val mBundle = Bundle()
                mBundle.putString("typeOfLeave", fileLeaveViewModel.selectedTypeOfLeave.value)
                mBundle.putInt("time", fileLeaveViewModel.selectedItem.value!!)
                mBundle.putString("startDate", fileLeaveViewModel.startDate.value)
                mBundle.putString("endDate", fileLeaveViewModel.endDate.value)
                mBundle.putParcelable("keyAccountDetails", fileLeaveViewModel.accountDetails.value)
                fragmentNavigatorInterface?.showFileLeaveSuccess(mBundle, FileLeaveSuccessFragment())
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
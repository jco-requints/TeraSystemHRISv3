package com.example.terasystemhrisv3.ui
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terasystemhrisv3.*
import com.example.terasystemhrisv3.adapter.RecyclerAdapter
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.interfaces.AppBarController
import com.example.terasystemhrisv3.interfaces.FragmentNavigator
import com.example.terasystemhrisv3.util.alertDialog
import com.example.terasystemhrisv3.viewmodel.LogsViewModel
import com.example.terasystemhrisv3.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_logs.view.*
import kotlinx.android.synthetic.main.fragment_logs.view.logsProgressBarHolder

class LogsFragment : Fragment() {

    private var myInterface: AppBarController? = null
    private var fragmentNavigatorInterface: FragmentNavigator? = null
    private lateinit var logsViewModel: LogsViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        if (bundle != null)
        {
            myDetails = bundle.getParcelable("keyAccountDetails")!!
        }
        logsViewModel = ViewModelProviders.of(this, ViewModelFactory { LogsViewModel(activity!!.application, myDetails) }).get(LogsViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(this.context)
        val view = inflater.inflate(R.layout.fragment_logs, container, false)
        myInterface?.setTitle(getString(R.string.logs_title))
        myInterface?.setAddButtonTitle("+")
        myInterface?.setCancelButtonTitle(null)
        myInterface?.getAddButton()?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36F)
        myInterface?.getAddButton()?.visibility = View.VISIBLE
        myInterface?.getCancelButton()?.visibility = View.GONE

        //Logic for + button
        myInterface?.getAddButton()?.setOnClickListener {
            logsViewModel.showAddTimeLog()
        }

        logsViewModel.accountDetails.observe(viewLifecycleOwner, Observer {
            if(it != null)
            {
                logsViewModel.getTimeLogs()
            }
        })

        logsViewModel.isAddTimeLogClicked.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                logsViewModel.isAddTimeLogClicked.value = false
                val mBundle = Bundle()
                mBundle.putParcelable("keyAccountDetails", myDetails)
                fragmentNavigatorInterface?.showAddTimeLog(mBundle, AddTimeLogFragment())
            }
        })

        logsViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            view.logsProgressBarHolder.visibility = if (it) View.VISIBLE
            else View.GONE
        })

        view?.recyclerView?.layoutManager = linearLayoutManager
        logsViewModel.logsList.observe(viewLifecycleOwner, Observer {
            adapter = RecyclerAdapter(it)
            view?.recyclerView?.adapter = adapter
        })

        logsViewModel.webServiceError.observe(viewLifecycleOwner, Observer { message ->
            if(!message.isNullOrEmpty())
            {
                this.context?.let { mContext -> alertDialog(mContext, message) }
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
        val TAG: String = LogsFragment::class.java.simpleName
        const val KEY_ACCOUNT_DETAILS = "keyAccountDetails"

        fun newInstance(accountDetails: AccountDetails) = LogsFragment().apply {
            arguments = bundleOf(KEY_ACCOUNT_DETAILS to accountDetails)
        }
    }

}
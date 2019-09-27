package com.example.terasystemhrisv3.ui

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.terasystemhrisv3.*
import com.example.terasystemhrisv3.adapter.LeavesRecyclerAdapter
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.interfaces.AppBarController
import com.example.terasystemhrisv3.interfaces.FragmentNavigator
import com.example.terasystemhrisv3.util.alertDialog
import com.example.terasystemhrisv3.viewmodel.LeavesViewModel
import kotlinx.android.synthetic.main.fragment_leaves.view.*

class LeavesFragment : Fragment() {

    private var myInterface: AppBarController? = null
    private var fragmentNavigatorInterface: FragmentNavigator? = null
    private lateinit var leavesViewModel: LeavesViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: LeavesRecyclerAdapter
    private var myDetails: AccountDetails = AccountDetails("","","","","","","","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = this.arguments
        leavesViewModel = ViewModelProviders.of(this).get(LeavesViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(this.context)
        if (bundle != null)
        {
            myDetails = bundle.getParcelable("keyAccountDetails")!!
        }
        val view = inflater.inflate(R.layout.fragment_leaves, container, false)
        myInterface?.setTitle(getString(R.string.leaves_title))
        myInterface?.setAddButtonTitle("+")
        myInterface?.setCancelButtonTitle(null)
        myInterface?.getAddButton()?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36F)
        myInterface?.getAddButton()?.visibility = View.VISIBLE
        myInterface?.getCancelButton()?.visibility = View.GONE

        //Logic for + button
        myInterface?.getAddButton()?.setOnClickListener {
            val mBundle = Bundle()
            mBundle.putParcelable("keyAccountDetails", myDetails)
            fragmentNavigatorInterface?.showFileLeave(mBundle, FileLeaveFragment())
        }

        leavesViewModel.accountDetails.value = myDetails

        leavesViewModel.getLeaves()

        leavesViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            view.leavesProgressBarHolder.visibility = if (it) View.VISIBLE
            else View.GONE
        })

        view?.leavesRecyclerView?.layoutManager = linearLayoutManager
        leavesViewModel.leavesList.observe(viewLifecycleOwner, Observer {
            adapter = LeavesRecyclerAdapter(it, leavesViewModel.remSL, leavesViewModel.remVL, leavesViewModel.showRemSLAndRemVL.value!!)
            view?.leavesRecyclerView?.adapter = adapter
        })

        leavesViewModel.showRecyclerView.observe(viewLifecycleOwner, Observer {
            view.leavesRecyclerView.visibility = if (it) View.VISIBLE
            else View.GONE
        })

        leavesViewModel.webServiceError.observe(viewLifecycleOwner, Observer { message ->
            this.context?.let { mContext -> alertDialog(mContext, message) }
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
        val TAG: String = LeavesFragment::class.java.simpleName
        fun newInstance(accountDetails:AccountDetails) = LeavesFragment().apply {
            val bundle = Bundle()
            bundle.putParcelable("keyAccountDetails", accountDetails)
            this.arguments = bundle
        }
    }

}
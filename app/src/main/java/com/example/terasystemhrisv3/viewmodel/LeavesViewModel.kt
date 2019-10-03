package com.example.terasystemhrisv3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.terasystemhrisv3.model.AccountDetails
import com.example.terasystemhrisv3.model.Leaves
import com.example.terasystemhrisv3.service.RetrofitFactory
import com.example.terasystemhrisv3.util.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.text.ParseException
import kotlin.collections.ArrayList
import java.util.concurrent.TimeUnit

class LeavesViewModel(application: Application, retrievedAccountDetails: AccountDetails) : AndroidViewModel(application) {

    var webServiceError = SingleLiveEvent<String>()
    var accountDetails = MutableLiveData<AccountDetails>()
    var leaves = MutableLiveData<Leaves>()
    var remVL: Double = 0.0
    var remSL: Double = 0.0
    private val job = SupervisorJob()
    private val coroutineContext = Dispatchers.IO + job
    lateinit var leavesHolder: Leaves
    var leavesList = MutableLiveData<ArrayList<Leaves>>()
    var leavesListHolder = ArrayList<Leaves>()
    var showProgressbar = MutableLiveData<Boolean>()
    var showRemSLAndRemVL = MutableLiveData<Boolean>()
    var showRecyclerView = MutableLiveData<Boolean>()
    var isFileLeaveClicked = MutableLiveData<Boolean>()

    init {
        showRecyclerView.value = false
        showRemSLAndRemVL.value = false
        showProgressbar.value = false
        isFileLeaveClicked.value = false
        accountDetails.value = retrievedAccountDetails
        getLeaves()
    }

    fun getLeaves(){
        if(isConnected(getApplication()))
        {
            showProgressbar.value = true
            showRecyclerView.value = false
            showRemSLAndRemVL.value = false
            val service = RetrofitFactory.makeRetrofitService()
            leavesList.value?.clear()
            leavesListHolder.clear()
            CoroutineScope(coroutineContext).launch {
                val response = service.GetLeaves(accountDetails.value?.userID)
                withContext(Dispatchers.Main) {
                    try {
                        if (response.isSuccessful) {
                            val details = response.body()
                            if(details?.status == "0")
                            {
                                remVL = 13.0
                                remSL = 13.0
                                for (i in 0 until details.leaves!!.count()) {
                                    leavesHolder = Leaves("","","","","")
                                    leavesHolder.userID = details.leaves!![i].userID
                                    leavesHolder.type = details.leaves!![i].type?.let { type -> convertLeaveTypeToReadableForm(type) }
                                    leavesHolder.dateFrom = details.leaves!![i].dateFrom?.let { dateFrom -> convertDateToHumanDate(dateFrom) }
                                    leavesHolder.dateTo = details.leaves!![i].dateTo?.let { dateTo -> convertDateToHumanDate(dateTo) }
                                    leavesHolder.time = convertTimeToReadableForm(details.leaves!![i].time.toString(), details.leaves!![i].dateFrom.toString(), details.leaves!![i].dateTo.toString())
                                    if(leavesHolder.type == "Vacation Leave")
                                    {
                                        remVL -= leavesHolder.time!!.toFloat()
                                    }
                                    else
                                    {
                                        remSL -= leavesHolder.time!!.toFloat()
                                    }
                                    leaves.value = leavesHolder
                                    leavesListHolder.add(leaves.value!!)
                                }
                                showRecyclerView.value = true
                                showRemSLAndRemVL.value = true
                                leavesList.value = leavesListHolder
                            }
                            else
                            {
                                webServiceError.value = response.body()?.message
                            }
                            showProgressbar.postValue(false)
                        } else {
                            webServiceError.postValue("Error: ${response.code()}")
                            showProgressbar.postValue(false)
                        }
                    } catch (e: HttpException) {
                        webServiceError.postValue("Exception ${e.message}")
                        showProgressbar.postValue(false)
                    } catch (e: Throwable) {
                        webServiceError.postValue(e.message)
                        showProgressbar.postValue(false)
                    }
                }
            }
        }
        else
        {
            leavesList.value?.clear()
            leavesListHolder.clear()
            showRecyclerView.value = false
            showRemSLAndRemVL.value = false
            webServiceError.value = "No Internet Connection"
        }
    }

    private fun convertLeaveTypeToReadableForm(leaveType: String): String {
        var convertedLeaveType = ""
        return try {
            convertedLeaveType = if(leaveType == "1") {
                "Vacation Leave"
            } else {
                "Sick Leave"
            }
            convertedLeaveType
        } catch (e: ParseException) {
            e.printStackTrace()
            convertedLeaveType
        }
    }

    private fun convertTimeToReadableForm(time: String, dateFrom: String, dateTo: String): String {
        var convertedTime = ""
        val total: Float
        try {
            if(time == "1")
            {
                if(dateTo.isEmpty())
                {
                    convertedTime = "1"
                }
                else
                {

                    val diffInMillisec = dateTo.toLong() - dateFrom.toLong()

                    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillisec)

                    total = diffInDays.toFloat() + 1
                    convertedTime = trimTrailingZero(total.toString())
                }
            }
            else
            {
                convertedTime = "0.5"
            }
            return convertedTime
        } catch (e: ParseException) {
            e.printStackTrace()
            return convertedTime
        }
    }

    private fun trimTrailingZero(value: String): String {
        return if (value.isNotEmpty()) {
            if (value.indexOf(".") < 0) {
                value

            } else {
                value.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
            }

        } else {
            value
        }
    }
}
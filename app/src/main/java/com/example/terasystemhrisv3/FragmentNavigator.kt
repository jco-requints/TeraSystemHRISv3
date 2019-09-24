package com.example.terasystemhrisv3

import android.os.Bundle
import androidx.fragment.app.Fragment

interface FragmentNavigator
{
    fun showAddTimeLog(mBundle: Bundle, fragment: Fragment)
    fun showAddTimeLogSuccess(mBundle: Bundle, fragment: Fragment)
    fun showFileLeave(mBundle: Bundle, fragment: Fragment)
    fun showFileLeaveSuccess(mBundle: Bundle, fragment: Fragment)
    fun showTimeLogs(mBundle: Bundle, fragment: Fragment)
    fun showLogDetails(mBundle: Bundle, fragment: Fragment)
}
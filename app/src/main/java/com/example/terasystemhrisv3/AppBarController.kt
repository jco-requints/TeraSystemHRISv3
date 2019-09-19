package com.example.terasystemhrisv3

import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager


interface AppBarController
{
    fun setTitle(title: String)
    fun setCancelButtonTitle(cancelTitle: String?)
    fun setAddButtonTitle(addTitle: String?)
    fun getToolBar(): Toolbar
    fun getCancelButton(): Button
    fun getAddButton(): Button
    fun getSupportFragmentManager(): FragmentManager
}
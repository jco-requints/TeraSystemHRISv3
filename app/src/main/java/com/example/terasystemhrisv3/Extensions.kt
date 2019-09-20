package com.example.terasystemhrisv3

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.example.terasystemhrisv3.ui.LoginActivity


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
  this.addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
      afterTextChanged.invoke(editable.toString())
    }
  })
}

fun alertDialog(context: Context, error: String) {
  val dialog = AlertDialog.Builder(context)
  dialog.setTitle(error)
  dialog.setCancelable(false)
  dialog.setNegativeButton("Ok",
    DialogInterface.OnClickListener { dialog, which ->
      dialog.cancel()
    })
  val alertDialog = dialog.create()
  alertDialog.show()
}

fun isConnected(context: Context): Boolean {
  val connectivity = context.getSystemService(
    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  if (connectivity != null) {
    val info = connectivity.allNetworkInfo
    if (info != null)
      for (i in info)
        if (i.state == NetworkInfo.State.CONNECTED) {
          return true
        }
  }
  return false
}
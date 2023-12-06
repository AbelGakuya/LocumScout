package com.locums.locumscout.other

import android.app.Activity
import android.app.AlertDialog
import com.locums.locumscout.R

class LoadingDialog(val mActivity: Activity) {
    private lateinit var dialogAlert: AlertDialog

    fun startLoadingDialog(){
        val builder = AlertDialog.Builder(mActivity)
        val inflater = mActivity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog, null))
        builder.setCancelable(true)

        dialogAlert = builder.create()

        dialogAlert.create()
        dialogAlert.show()
    }

    fun dismissDialog(){
        dialogAlert.dismiss()
    }
}
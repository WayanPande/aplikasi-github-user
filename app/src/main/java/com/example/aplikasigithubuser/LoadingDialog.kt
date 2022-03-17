package com.example.aplikasigithubuser

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog

class LoadingDialog(myActivity: Activity) {

    private val activity: Activity = myActivity
    private lateinit var dialog: AlertDialog

<<<<<<< HEAD
=======

    @SuppressLint("InflateParams")
>>>>>>> e0a4c8b... refactoring all API request using viewModel and fixing some bugs
    fun startLoadingDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun dismissDialog(){
        if (this::dialog.isInitialized){
            dialog.dismiss()
        }
    }

}
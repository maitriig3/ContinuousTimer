package com.hkngtech.continuoustimer.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController


fun Fragment.navigate(action: NavDirections){
    findNavController().navigate(action)
}

fun String.toast(context: Context,length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(context,this,length).show()
}

fun String.logE() = Log.e("ERR",this)
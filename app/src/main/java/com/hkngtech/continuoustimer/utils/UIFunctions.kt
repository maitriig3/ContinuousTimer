package com.hkngtech.continuoustimer.utils

import android.graphics.Color
import com.google.android.material.textfield.TextInputLayout

fun editTextValidation(editTextInputLayout: TextInputLayout,validate: String){
    if(validate.isEmpty()){
        editTextInputLayout.boxStrokeColor = Color.GREEN
        editTextInputLayout.error = null
        editTextInputLayout.isErrorEnabled = false
    }else{
        editTextInputLayout.boxStrokeColor = Color.RED
        editTextInputLayout.error = validate
    }
}
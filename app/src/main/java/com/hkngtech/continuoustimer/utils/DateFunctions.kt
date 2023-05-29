package com.hkngtech.continuoustimer.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getTimeInMillis() = Calendar.getInstance().timeInMillis

fun getSimpleDateFormat(format: String) = SimpleDateFormat(format, Locale.getDefault())

fun getCurrentDate(format: String) = getSimpleDateFormat(format).format(Calendar.getInstance().time)
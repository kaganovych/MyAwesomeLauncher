package com.example.myawesomelauncher.presentation.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.toast(@StringRes res: Int, length: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(activity, res, length).show()
}
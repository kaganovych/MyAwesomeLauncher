package com.example.myawesomelauncher.presentation.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.myawesomelauncher.R

data class AppInfoViewModel(
  var title: String = "",
  var packageName: String = "",
  var image: Drawable? = null) {

  fun getIcon(context: Context): Drawable? {
    return image ?: ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
  }
}
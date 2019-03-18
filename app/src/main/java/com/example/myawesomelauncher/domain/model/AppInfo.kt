package com.example.myawesomelauncher.domain.model

import android.graphics.drawable.Drawable

data class AppInfo(var title: String = "",
              var packageName: String = "",
              var image: Drawable? = null)
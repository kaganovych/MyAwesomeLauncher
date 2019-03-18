package com.example.myawesomelauncher

import android.app.Application
import com.example.myawesomelauncher.di.app.AppComponent
import com.example.myawesomelauncher.di.app.ContextModule
import com.example.myawesomelauncher.di.app.DaggerAppComponent

class LauncherApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    AppComponent.init(DaggerAppComponent.builder()
      .contextModule(ContextModule(this))
      .build())
  }
}
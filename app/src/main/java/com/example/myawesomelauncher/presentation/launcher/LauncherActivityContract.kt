package com.example.myawesomelauncher.presentation.launcher

import com.example.myawesomelauncher.presentation.model.AppInfoViewModel

interface LauncherActivityContract {
  interface View {
    fun onGetAppsSuccess(list: List<AppInfoViewModel>)
  }

  interface Presenter {
    fun getAllApps()
    fun unsubscribe()
    fun setView(view: LauncherActivityContract.View)
  }
}
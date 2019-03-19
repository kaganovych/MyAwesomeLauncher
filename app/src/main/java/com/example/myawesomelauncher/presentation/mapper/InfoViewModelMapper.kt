package com.example.myawesomelauncher.presentation.mapper

import com.example.myawesomelauncher.base.mapper.Mapper
import com.example.myawesomelauncher.domain.model.AppInfo
import com.example.myawesomelauncher.presentation.model.AppInfoViewModel
import javax.inject.Inject

open class InfoViewModelMapper @Inject constructor(): Mapper<AppInfo, AppInfoViewModel>() {
  override fun reverse(to: AppInfoViewModel): AppInfo {
    TODO("not needed")
  }

  override fun map(from: AppInfo): AppInfoViewModel {
    return AppInfoViewModel().apply {
      title = from.title
      packageName = from.packageName
      image = from.image
    }
  }
}
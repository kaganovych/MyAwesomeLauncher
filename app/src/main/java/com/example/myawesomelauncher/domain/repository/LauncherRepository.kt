package com.example.myawesomelauncher.domain.repository

import com.example.myawesomelauncher.domain.model.AppInfo
import io.reactivex.Single

interface LauncherRepository {
  fun getAllApps(): Single<List<AppInfo>>
}
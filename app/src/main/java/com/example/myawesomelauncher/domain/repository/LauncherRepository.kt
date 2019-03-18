package com.example.myawesomelauncher.domain.repository

import com.example.myawesomelauncher.domain.model.AppInfo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface LauncherRepository {
  fun getAllApps(): Single<List<AppInfo>>
  fun subscribeToApps(): BehaviorSubject<List<AppInfo>>
}
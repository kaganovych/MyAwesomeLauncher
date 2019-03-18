package com.example.myawesomelauncher.data.repository

import android.content.Intent
import android.content.pm.PackageManager
import com.example.myawesomelauncher.domain.model.AppInfo
import com.example.myawesomelauncher.domain.repository.LauncherRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LauncherRepositoryImpl @Inject constructor(
  private val packageManager: PackageManager,
  private val packageIntent: Intent) : LauncherRepository {

  private val appsSubject = BehaviorSubject.create<List<AppInfo>>()

  override fun subscribeToApps(): BehaviorSubject<List<AppInfo>> {
    return appsSubject
  }

  override fun getAllApps(): Single<List<AppInfo>> {
    return Single.create<List<AppInfo>> {
      try {
        val list = packageManager.queryIntentActivities(packageIntent, 0)
          .map {
            AppInfo().apply {
              title = it.loadLabel(packageManager).toString()
              packageName = it.activityInfo.packageName
              image = it.activityInfo.loadIcon(packageManager)
            }
          }
        it.onSuccess(list)

      } catch (e: Throwable) {
        it.onError(e)
      }
    }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnSuccess {
        appsSubject.onNext(it)
      }
  }
}
package com.example.myawesomelauncher.data.repository

import com.example.myawesomelauncher.base.package_manager.PackageManagerProvider
import com.example.myawesomelauncher.domain.model.AppInfo
import com.example.myawesomelauncher.domain.repository.LauncherRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LauncherRepositoryImpl @Inject constructor(
  private val packageManagerProvider: PackageManagerProvider) : LauncherRepository {

  override fun getAllApps(): Single<List<AppInfo>> {
    return Single.create<List<AppInfo>> {
      try {
        val list = packageManagerProvider.getAllApps()
          .map {
            AppInfo().apply {
              title = packageManagerProvider.getLabel(it)
              packageName = packageManagerProvider.getPackageName(it)
              image = packageManagerProvider.getIcon(it)
            }
          }
        it.onSuccess(list)
      } catch (e: Throwable) {
        it.onError(e)
      }
    }
  }
}
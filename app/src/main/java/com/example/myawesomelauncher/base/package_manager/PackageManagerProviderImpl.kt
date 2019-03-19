package com.example.myawesomelauncher.base.package_manager

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PackageManagerProviderImpl @Inject constructor(
  private val packageManager: PackageManager,
  private val packageIntent: Intent) : PackageManagerProvider {
  override fun getPackageName(resolveInfo: ResolveInfo): String {
    return resolveInfo.activityInfo.packageName
  }

  override fun getLabel(resolveInfo: ResolveInfo): String {
    return resolveInfo.loadLabel(packageManager).toString()
  }

  override fun getIcon(resolveInfo: ResolveInfo): Drawable? {
    return resolveInfo.loadIcon(packageManager)
  }

  override fun getAllApps(): List<ResolveInfo> {
    return packageManager.queryIntentActivities(packageIntent, 0)
  }
}
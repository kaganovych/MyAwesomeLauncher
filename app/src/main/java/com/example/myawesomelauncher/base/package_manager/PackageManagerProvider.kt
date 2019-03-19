package com.example.myawesomelauncher.base.package_manager

import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

interface PackageManagerProvider {
  fun getAllApps(): List<ResolveInfo>
  fun getLabel(resolveInfo: ResolveInfo): String
  fun getIcon(resolveInfo: ResolveInfo): Drawable?
  fun getPackageName(resolveInfo: ResolveInfo): String
}
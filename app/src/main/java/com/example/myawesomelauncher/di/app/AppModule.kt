package com.example.myawesomelauncher.di.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.myawesomelauncher.base.package_manager.PackageManagerProvider
import com.example.myawesomelauncher.base.package_manager.PackageManagerProviderImpl
import com.example.myawesomelauncher.data.repository.LauncherRepositoryImpl
import com.example.myawesomelauncher.domain.repository.LauncherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

  @Binds
  @Singleton
  abstract fun provideRepository(impl: LauncherRepositoryImpl): LauncherRepository

  @Binds
  @Singleton
  abstract fun providePackageManagerProvider(impl: PackageManagerProviderImpl): PackageManagerProvider

  @Module
  companion object {

    @JvmStatic
    @Provides
    @Singleton
    fun providePackageManager(context: Context): PackageManager {
      return context.packageManager
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideLauncherIntent(): Intent {
      return Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
      }
    }
  }
}
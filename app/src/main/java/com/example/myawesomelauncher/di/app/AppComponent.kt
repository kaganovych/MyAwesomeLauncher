package com.example.myawesomelauncher.di.app

import com.example.myawesomelauncher.domain.repository.LauncherRepository
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ContextModule::class])
@Singleton
interface AppComponent {
  fun repository(): LauncherRepository

  companion object {
    @Volatile
    private lateinit var appComponent: AppComponent

    fun init(appComponent: AppComponent) {
      if (this::appComponent.isInitialized) {
        throw IllegalArgumentException("BaseAppComponent is already initialized.")
      }

      this.appComponent = appComponent
    }

    fun get(): AppComponent {
      if (!this::appComponent.isInitialized) {
        throw NullPointerException("BaseAppComponent is not initialized yet. Call init first.")
      }

      return appComponent
    }
  }
}
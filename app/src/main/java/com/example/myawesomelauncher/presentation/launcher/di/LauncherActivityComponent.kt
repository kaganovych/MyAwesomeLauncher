package com.example.myawesomelauncher.presentation.launcher.di

import com.example.myawesomelauncher.di.app.AppComponent
import com.example.myawesomelauncher.di.scope.PerActivity
import com.example.myawesomelauncher.presentation.launcher.LauncherActivity
import dagger.Component

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [LauncherActivityModule::class])
interface LauncherActivityComponent {
  fun inject(activity: LauncherActivity)
}
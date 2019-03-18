package com.example.myawesomelauncher.presentation.launcher.di

import com.example.myawesomelauncher.di.scope.PerActivity
import com.example.myawesomelauncher.presentation.launcher.LauncherActivityContract
import com.example.myawesomelauncher.presentation.launcher.LauncherActivityPresenterImpl
import dagger.Binds
import dagger.Module

@Module
abstract class LauncherActivityModule {

  @Binds
  @PerActivity
  abstract fun providePresenter(impl: LauncherActivityPresenterImpl): LauncherActivityContract.Presenter
}
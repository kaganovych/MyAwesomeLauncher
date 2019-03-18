package com.example.myawesomelauncher.domain.interactor

import com.example.myawesomelauncher.base.interactor.ObservableUseCaseWithoutParams
import com.example.myawesomelauncher.domain.model.AppInfo
import com.example.myawesomelauncher.domain.repository.LauncherRepository
import io.reactivex.Observable
import javax.inject.Inject

class SubscribeToAllAppsUseCase @Inject constructor(private val repository: LauncherRepository): ObservableUseCaseWithoutParams<List<AppInfo>>() {
  override fun buildUseCaseObservable(): Observable<List<AppInfo>> {
    return repository.subscribeToApps()
  }
}
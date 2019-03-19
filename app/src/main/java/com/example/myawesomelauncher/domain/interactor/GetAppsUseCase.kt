package com.example.myawesomelauncher.domain.interactor

import com.example.myawesomelauncher.base.interactor.SingleUseCaseWithoutParams
import com.example.myawesomelauncher.domain.model.AppInfo
import com.example.myawesomelauncher.domain.repository.LauncherRepository
import io.reactivex.Single
import javax.inject.Inject

open class GetAppsUseCase @Inject constructor(private val repository: LauncherRepository): SingleUseCaseWithoutParams<List<AppInfo>>(){
  override fun buildUseCaseObservable(): Single<List<AppInfo>> {
    return repository.getAllApps()
  }
}
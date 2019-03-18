package com.example.myawesomelauncher.presentation.launcher

import com.example.myawesomelauncher.di.scope.PerActivity
import com.example.myawesomelauncher.domain.interactor.GetAppsUseCase
import com.example.myawesomelauncher.presentation.extensions.plusAssign
import com.example.myawesomelauncher.presentation.mapper.InfoViewModelMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@PerActivity
class LauncherActivityPresenterImpl @Inject constructor(
  private val getAppsUseCase: GetAppsUseCase,
  private val appInfoViewModelMapper: InfoViewModelMapper): LauncherActivityContract.Presenter  {

  private val disposables = CompositeDisposable()

  private var view: LauncherActivityContract.View? = null

  override fun setView(view: LauncherActivityContract.View) {
    this.view = view
  }

  override fun unsubscribe() {
    disposables.clear()
    view = null
  }

  override fun getAllApps() {
    disposables += getAppsUseCase.execute()
      .map { appInfoViewModelMapper.map(it).sortedBy { model -> model.title } }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        view?.onGetAppsSuccess(it)
      }, {
        it.printStackTrace()
      })
  }
}
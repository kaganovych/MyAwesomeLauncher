package com.example.myawesomelauncher.presentation.launcher

import com.example.myawesomelauncher.BaseTest
import com.example.myawesomelauncher.domain.interactor.GetAppsUseCase
import com.example.myawesomelauncher.domain.model.AppInfo
import com.example.myawesomelauncher.presentation.model.AppInfoViewModel
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class LauncherActivityPresenterImplTest : BaseTest() {

  @Mock
  private lateinit var view: LauncherActivityContract.View

  @Mock
  private lateinit var getAppsUseCase: GetAppsUseCase

  private lateinit var presenter: LauncherActivityContract.Presenter

  private val listOfInfos = listOf(AppInfo("a", "a"), AppInfo("b", "b"))

  override fun setup() {
    super.setup()

    presenter = LauncherActivityPresenterImpl(getAppsUseCase)
    presenter.setView(view)
  }

  @Test
  fun get_all_apps_success() {
    `when`(getAppsUseCase.execute()).thenReturn(Single.just(listOfInfos))

    presenter.getAllApps()

    verify(view).onGetAppsSuccess(listOfInfos.map { AppInfoViewModel(it.title, it.packageName, it.image) }.sortedBy { it.title })
  }

  @Test
  fun get_all_apps_failure() {
    `when`(getAppsUseCase.execute()).thenReturn(Single.error(exception))

    presenter.getAllApps()

    verify(view).onGetAppsFailure()
  }
}
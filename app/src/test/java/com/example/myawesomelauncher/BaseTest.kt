package com.example.myawesomelauncher

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.mockito.MockitoAnnotations

open class BaseTest {

  val exception = Throwable()

  @Before
  open fun setup() {
    MockitoAnnotations.initMocks(this)
  }

  companion object {
    @JvmStatic
    @BeforeClass
    fun beforeClass() {
      RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
      RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @JvmStatic
    @AfterClass
    fun afterClass() {
      RxAndroidPlugins.reset()
      RxJavaPlugins.reset()
    }
  }
}
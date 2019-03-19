package com.example.myawesomelauncher.presentation.launcher

import android.content.*
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myawesomelauncher.R
import com.example.myawesomelauncher.di.app.AppComponent
import com.example.myawesomelauncher.presentation.all_apps.adapter.AllAppsAdapter
import com.example.myawesomelauncher.presentation.all_apps.adapter.AppViewHolder
import com.example.myawesomelauncher.presentation.all_apps.adapter.GridItemPaddingDecorator
import com.example.myawesomelauncher.presentation.extensions.toast
import com.example.myawesomelauncher.presentation.launcher.adapter.DesktopPagerAdapter
import com.example.myawesomelauncher.presentation.launcher.di.DaggerLauncherActivityComponent
import com.example.myawesomelauncher.presentation.launcher.di.LauncherActivityComponent
import com.example.myawesomelauncher.presentation.model.AppInfoViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class LauncherActivity : AppCompatActivity(), LauncherActivityContract.View {

  @Inject
  lateinit var presenter: LauncherActivityContract.Presenter

  private lateinit var component: LauncherActivityComponent

  @Inject
  lateinit var desktopPagerAdapter: DesktopPagerAdapter

  @Inject
  lateinit var allAppsAdapter: AllAppsAdapter

  private var dragType = DragType.DESKTOP

  private lateinit var behavior: BottomSheetBehavior<RecyclerView>

  private val broadcastReceiver = UninstallReceiver()

  override fun onCreate(savedInstanceState: Bundle?) {
    injectComponent()
    super.onCreate(savedInstanceState)
    setContentView(com.example.myawesomelauncher.R.layout.activity_main)

    desktopPagerAdapter.appsListener = allAppsListener

    with(mainViewPager) {
      adapter = desktopPagerAdapter
      offscreenPageLimit = 3
    }

    mainViewPager.setOnDragListener(allAppsDragListener)
    mainTvDelete.setOnDragListener(allAppsDragListener)
    mainTvInfo.setOnDragListener(allAppsDragListener)

    mainPageIndicator.setViewPager(mainViewPager)

    with(mainRvAllApps) {
      adapter = allAppsAdapter
      val spanCount = 4
      layoutManager = GridLayoutManager(context, spanCount)
      setHasFixedSize(true)
      addItemDecoration(
        GridItemPaddingDecorator(
          context.resources.getDimensionPixelOffset(com.example.myawesomelauncher.R.dimen.item_padding),
          spanCount
        )
      )
    }

    IntentFilter().apply {
      addAction(Intent.ACTION_PACKAGE_ADDED)
      addAction(Intent.ACTION_PACKAGE_INSTALL)
      addDataScheme("package")
      registerReceiver(broadcastReceiver, this)
    }

    behavior = BottomSheetBehavior.from(mainRvAllApps)
    mainRoot.doOnLayout {
      behavior.peekHeight = it.height
      behavior.isHideable = true
      behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    mainAllAppsIv.setOnClickListener {
      behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    allAppsAdapter.listener = allAppsListener
  }

  override fun onGetAppsSuccess(list: List<AppInfoViewModel>) {
    mainRvAllApps.isVisible = true
    allAppsAdapter.swapData(list)
  }

  override fun onGetAppsFailure() {
    toast(R.string.something_went_wrong)
  }

  private val allAppsListener = object : AppViewHolder.Listener {
    override fun onAppClick(packageName: String) {
      startActivity(packageManager.getLaunchIntentForPackage(packageName))
    }

    override fun onLongClick(view: View, appInfoViewModel: AppInfoViewModel) {
      val data = ClipData.newPlainText("", "")
      val shadowBuilder = MyDragShadowBuilder(view)
      view.startDrag(data, shadowBuilder, appInfoViewModel, 0)
      dragType = if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) DragType.DESKTOP else DragType.FROM_APPS
      behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
  }

  private class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

    override fun onProvideShadowMetrics(size: Point, touch: Point) {
      val width: Int = view.width
      val height: Int = view.height

      size.set(width, height)

      touch.set(width, height / 2)
    }
  }

  override fun onStart() {
    super.onStart()
    presenter.setView(this)
    presenter.getAllApps()
  }

  override fun onStop() {
    super.onStop()
    presenter.unsubscribe()
  }

  override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(broadcastReceiver)
  }

  private val allAppsDragListener = View.OnDragListener { view, event ->
    when (event.action) {
      DragEvent.ACTION_DRAG_STARTED -> {
        mainGroup.isVisible = true
        mainTvDelete.text = if (dragType == DragType.DESKTOP) "Remove" else "Delete"
        true
      }
      DragEvent.ACTION_DROP -> {

        val info = event.localState as AppInfoViewModel
        mainGroup.isVisible = false
        when(view.id) {
          R.id.mainViewPager -> {
            if (dragType == DragType.DESKTOP) return@OnDragListener false
            addAppToDesktop(info)
          }
          R.id.mainTvDelete -> {
            when(dragType) {
              DragType.FROM_APPS -> deleteApp(info)
              DragType.DESKTOP -> removeApp(info)
            }
          }
          R.id.mainTvInfo -> {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:${info.packageName}")
            startActivity(intent)
          }
        }
        true
      }
      else -> {
        Log.d(LauncherActivity::class.java.name, "Unknown action type received by OnDragListener.")
        false
      }
    }
  }

  private fun addAppToDesktop(info: AppInfoViewModel) {
    val position = mainViewPager.currentItem
    val rv = mainViewPager.findViewWithTag<RecyclerView>(position)

    (rv.adapter as AllAppsAdapter).addApp(info)
  }

  private fun removeApp(info: AppInfoViewModel) {
    val position = mainViewPager.currentItem
    val rv = mainViewPager.findViewWithTag<RecyclerView>(position)
    (rv.adapter as AllAppsAdapter).removeApp(info.packageName)
  }

  private fun deleteApp(info: AppInfoViewModel) {
    val intent = Intent(Intent.ACTION_DELETE)
    intent.data = Uri.parse("package:${info.packageName}")
    intent.putExtra("packageName", info.packageName)
    startActivity(intent)
  }

  private fun injectComponent() {
    component = DaggerLauncherActivityComponent
      .builder()
      .appComponent(AppComponent.get())
      .build()
    component.inject(this)
  }

  enum class DragType {
    FROM_APPS, DESKTOP
  }

  override fun onBackPressed() {
    if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
      behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
  }

  inner class UninstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      if (intent == null) return
      val packageName = intent.getStringExtra("packageName") ?: return
      mainViewPager?.forEach {
        ((it as RecyclerView).adapter as AllAppsAdapter).removeApp(packageName)
      }
      allAppsAdapter.removeApp(packageName)
    }
  }
}

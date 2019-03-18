package com.example.myawesomelauncher.presentation.launcher.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.myawesomelauncher.R
import com.example.myawesomelauncher.di.scope.PerActivity
import com.example.myawesomelauncher.presentation.all_apps.adapter.AllAppsAdapter
import com.example.myawesomelauncher.presentation.all_apps.adapter.AppViewHolder
import com.example.myawesomelauncher.presentation.all_apps.adapter.GridItemPaddingDecorator
import javax.inject.Inject

@PerActivity
class DesktopPagerAdapter @Inject constructor() : PagerAdapter() {

  private val numberOfPages = 3

  var appsListener: AppViewHolder.Listener? = null

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val recyclerView = RecyclerView(container.context).apply {
      adapter = AllAppsAdapter().apply {
        listener = appsListener
      }
      val spanCount = 4
      val space = context.resources.getDimensionPixelOffset(R.dimen.item_padding)
      layoutManager = GridLayoutManager(context, spanCount)
      addItemDecoration(GridItemPaddingDecorator(space, spanCount))
      setHasFixedSize(true)
    }
    recyclerView.tag = position
    container.addView(recyclerView)
    return recyclerView
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    container.removeView(`object` as View)
  }

  override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return view == `object`
  }

  override fun getCount(): Int {
    return numberOfPages
  }
}
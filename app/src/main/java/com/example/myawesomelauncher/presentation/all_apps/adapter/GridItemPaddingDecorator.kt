package com.example.myawesomelauncher.presentation.all_apps.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemPaddingDecorator(private val space: Int, private val spanCount: Int): RecyclerView.ItemDecoration() {

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    super.getItemOffsets(outRect, view, parent, state)
    val position = parent.getChildAdapterPosition(view) // item position
    val column = position % spanCount // item column

    outRect.left = space - column * space / spanCount
    outRect.right = (column + 1) * space / spanCount

    if (position < spanCount) {
      outRect.top = space
    }
    outRect.bottom = space
  }
}
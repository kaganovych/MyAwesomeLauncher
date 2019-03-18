package com.example.myawesomelauncher.presentation.all_apps.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myawesomelauncher.presentation.model.AppInfoViewModel
import kotlinx.android.synthetic.main.item_app.view.*

class AppViewHolder(
  view: View,
  private val listener: AppViewHolder.Listener?) : RecyclerView.ViewHolder(view) {


  fun bind(appInfoViewModel: AppInfoViewModel) {
    with(itemView) {

      itemAppTvTitle.text = appInfoViewModel.title
      itemAppIv.background = appInfoViewModel.getIcon(context)

      setOnClickListener {
        listener?.onAppClick(appInfoViewModel.packageName)
      }

      setOnLongClickListener {
        listener?.onLongClick(itemAppIv, appInfoViewModel)
        return@setOnLongClickListener true
      }
    }
  }

  interface Listener {
    fun onAppClick(packageName: String)
    fun onLongClick(view: View, appInfoViewModel: AppInfoViewModel)
  }
}
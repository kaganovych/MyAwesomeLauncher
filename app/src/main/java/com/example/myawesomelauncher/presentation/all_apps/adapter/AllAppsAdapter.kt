package com.example.myawesomelauncher.presentation.all_apps.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myawesomelauncher.R
import com.example.myawesomelauncher.di.scope.PerActivity
import com.example.myawesomelauncher.presentation.extensions.inflate
import com.example.myawesomelauncher.presentation.model.AppInfoViewModel
import javax.inject.Inject

@PerActivity
class AllAppsAdapter @Inject constructor() : RecyclerView.Adapter<AppViewHolder>() {

  private val currentList = mutableListOf<AppInfoViewModel>()

  var listener: AppViewHolder.Listener? = null

  fun swapData(list: List<AppInfoViewModel>) {
    with(currentList) {
      clear()
      addAll(list)
      notifyDataSetChanged()
    }
  }

  fun addApp(appInfoViewModel: AppInfoViewModel) {
    currentList.add(currentList.size, appInfoViewModel)
    notifyItemInserted(currentList.size)
  }

  fun removeApp(packageName: String) {
    val item = currentList.firstOrNull { it.packageName == packageName } ?: return
    val index = currentList.indexOf(item)
    currentList.remove(item)
    notifyItemRemoved(index)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
    return AppViewHolder(parent.inflate(R.layout.item_app), listener)
  }

  override fun getItemCount(): Int {
    return currentList.size
  }

  override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
    holder.bind(currentList[position])
  }
}
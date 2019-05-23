package ru.komcity.mobile.UI.Holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseHolder<IL>(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
    abstract fun setData(item: IL)
    abstract fun setItemClickListener(listener: View.OnClickListener?)
}
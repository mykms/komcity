package ru.komcity.mobile.UI.Holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder<IL>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun setData(item: IL)
    abstract fun setItemClickListener(listener: View.OnClickListener?)
}
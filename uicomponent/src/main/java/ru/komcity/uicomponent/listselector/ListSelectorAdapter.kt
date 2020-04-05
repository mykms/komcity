package ru.komcity.uicomponent.listselector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ui_list_selector_item.view.*
import ru.komcity.uicomponent.R

/**
 * Created by Aleksei Kholoimov on 05.04.2020
 * <p>
 * Для показа в списке выбора (диалога)
 */
class ListSelectorAdapter(private val items: List<String>,
                          private val listener: (item: String, position: Int) -> Unit) : RecyclerView.Adapter<ListSelectorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ui_list_selector_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String, position: Int) {
            with(itemView) {
                tvTitle.text = item
                setOnClickListener { listener(item, position) }
            }
        }
    }
}
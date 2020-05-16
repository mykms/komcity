package ru.komcity.mobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_announcement.view.*
import ru.komcity.mobile.R
import ru.komcity.mobile.viewModel.Announcement

/**
 * Created by Aleksei Kholoimov on 06.04.2020
 * <p>
 * Adapter for announcements
 */
class AnnouncementAdapter(val items: List<Announcement>, private val onItemClick: (item: Announcement) -> Unit)
    : RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Announcement) {
            itemView.tvDescription.text = item.description
            itemView.tvDescription.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
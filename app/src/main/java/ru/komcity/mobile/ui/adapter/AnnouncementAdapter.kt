package ru.komcity.mobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.komcity.mobile.databinding.ItemAnnouncementBinding
import ru.komcity.mobile.viewModel.Announcement

/**
 * Created by Aleksei Kholoimov on 06.04.2020
 * <p>
 * Adapter for announcements
 */
class AnnouncementAdapter(val items: List<Announcement>, private val onItemClick: (item: Announcement) -> Unit)
    : RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {

    private var _binding: ItemAnnouncementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemAnnouncementBinding.inflate(LayoutInflater.from(parent.context), parent, true)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Announcement) {
            binding.tvDescription.text = item.description
            binding.tvDescription.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
package ru.komcity.mobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_news_add_file.view.*
import ru.komcity.mobile.R
import ru.komcity.mobile.viewModel.addnews.AddNewsBaseItem
import ru.komcity.mobile.viewModel.addnews.AddNewsItemEmpty
import ru.komcity.mobile.viewModel.addnews.AddNewsItemFile

/**
 * Created by Aleksei Kholoimov on 10.05.2020
 * <p>
 * Adapter for add file for sending news
 */
class NewsAddFileAdapter(private val items: List<AddNewsBaseItem>, private val onItemClick: (isEditFile: Boolean) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_add_file, parent, false)
                ViewHolderItemFile(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_add_empty, parent, false)
                ViewHolderItemEmpty(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is AddNewsItemFile -> 0
            else -> 1
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as? ViewHolderItemFile)?.bind(items[position] as AddNewsItemFile)
            else -> (holder as? ViewHolderItemEmpty)?.bind(items[position] as AddNewsItemEmpty)
        }
    }

    inner class ViewHolderItemFile(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: AddNewsItemFile) {
            with(itemView) {
                tvFileName.text = item.file.path
                tvFileName.setOnClickListener { onItemClick(true) }
                setOnClickListener { onItemClick(true) }
            }
        }
    }

    inner class ViewHolderItemEmpty(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: AddNewsItemEmpty) {
            itemView.setOnClickListener { onItemClick(false) }
        }
    }
}
package ru.komcity.mobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_forum_message.view.*
import ru.komcity.mobile.R
import ru.komcity.mobile.viewModel.ForumMessagesItem

/**
 * Created by Aleksei Kholoimov on 11.05.2020
 * <p>
 * Adapter for forum messages
 */
class ForumMessageAdapter(private val items: List<ForumMessagesItem>,
                          private val onActionClick: (text: String, isShare: Boolean) -> Unit) : RecyclerView.Adapter<ForumMessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forum_message, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ForumMessagesItem) {
            with(itemView) {
                tvUserName.text = item.userName
                tvDateTime.text = item.date
                tvDescription.text = item.message
                ivCopyText.setOnClickListener {
                    onActionClick(item.message, false)
                }
                ivShareText.setOnClickListener {
                    onActionClick(item.message, true)
                }
            }
        }
    }
}
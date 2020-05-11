package ru.komcity.mobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_sub_forum.view.*
import ru.komcity.mobile.R
import ru.komcity.mobile.viewModel.SubForumItem

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Адаптер для списка подфорумов
 */
class SubForumAdapter(private val items: List<SubForumItem>,
                      private val listener: (title: String, forumId: String) -> Unit) : RecyclerView.Adapter<SubForumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sub_forum, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SubForumItem) {
            with(itemView) {
                tvTitle.text = item.forumName
                tvThemeCount.text = item.countTheme
                tvReplicCount.text = item.countReplic
                tvDescription.text = item.description
                initClickListener(this, item)
            }
        }

        private fun initClickListener(itemView: View, item: SubForumItem) {
            with(itemView) {
                tvTitle.setOnClickListener {listener(item.forumName, item.linkForum)  }
                tvThemeCount.setOnClickListener {listener(item.forumName, item.linkForum)  }
                tvReplicCount.setOnClickListener {listener(item.forumName, item.linkForum)  }
                tvDescription.setOnClickListener {listener(item.forumName, item.linkForum)  }
                setOnClickListener { listener(item.forumName, item.linkForum) }
            }
        }
    }
}
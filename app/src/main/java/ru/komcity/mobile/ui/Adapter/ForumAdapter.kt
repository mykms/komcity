package ru.komcity.mobile.ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_forum.view.*
import ru.komcity.mobile.R
import ru.komcity.mobile.viewModel.ForumItem

/**
 * Created by Aleksey on 2020.02.28
 * <p>
 * Адаптер для списка форумов
 */
class ForumAdapter(private val items: List<ForumItem>,
                   private val listener: (item: String) -> Unit) : RecyclerView.Adapter<ForumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forum, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ForumItem) {
            with(itemView) {
                tvTitle.text = item.forumName
                tvThemeCount.text = item.countTheme
                tvReplicCount.text = item.countReplic
                tvDescription.text = item.description
                initClickListener(this, item)
            }
        }

        private fun initClickListener(itemView: View, item: ForumItem) {
            with(itemView) {
                tvTitle.setOnClickListener {listener(item.linkForum)  }
                tvThemeCount.setOnClickListener {listener(item.linkForum)  }
                tvReplicCount.setOnClickListener {listener(item.linkForum)  }
                tvDescription.setOnClickListener {listener(item.linkForum)  }
                setOnClickListener { listener(item.linkForum) }
            }
        }
    }
}
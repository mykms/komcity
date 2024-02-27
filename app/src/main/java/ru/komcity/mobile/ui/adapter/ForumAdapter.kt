package ru.komcity.mobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.komcity.mobile.databinding.ItemForumBinding
import ru.komcity.mobile.viewModel.ForumItem

/**
 * Created by Aleksey on 2020.02.28
 * <p>
 * Адаптер для списка форумов
 */
class ForumAdapter(private val items: List<ForumItem>,
                   private val listener: (title: String, forumName: String) -> Unit) : RecyclerView.Adapter<ForumAdapter.ViewHolder>() {
    private var _binding: ItemForumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemForumBinding.inflate(LayoutInflater.from(parent.context), parent, true)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ForumItem) {
            with(binding) {
                tvTitle.text = item.forumName
                tvThemeCount.text = item.countTheme
                tvReplicCount.text = item.countReplic
                tvDescription.text = item.description
                initClickListener(root, item)
            }
        }

        private fun initClickListener(itemView: View, item: ForumItem) {
            with(binding) {
                tvTitle.setOnClickListener {listener(item.forumName, item.linkForum)  }
                tvThemeCount.setOnClickListener {listener(item.forumName, item.linkForum)  }
                tvReplicCount.setOnClickListener {listener(item.forumName, item.linkForum)  }
                tvDescription.setOnClickListener {listener(item.forumName, item.linkForum)  }
                root.setOnClickListener { listener(item.forumName, item.linkForum) }
            }
        }
    }
}
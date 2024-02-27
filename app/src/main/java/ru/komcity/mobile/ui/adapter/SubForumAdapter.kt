package ru.komcity.mobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.komcity.mobile.databinding.ItemSubForumBinding
import ru.komcity.mobile.viewModel.SubForumItem

/**
 * Created by Aleksey on 2020.03.01
 * <p>
 * Адаптер для списка подфорумов
 */
class SubForumAdapter(private val items: List<SubForumItem>,
                      private val listener: (title: String, forumId: String) -> Unit) : RecyclerView.Adapter<SubForumAdapter.ViewHolder>() {

    private var _binding: ItemSubForumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemSubForumBinding.inflate(LayoutInflater.from(parent.context), parent, true)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SubForumItem) {
            with(binding) {
                tvTitle.text = item.forumName
                tvThemeCount.text = item.countTheme
                tvReplicCount.text = item.countReplic
                tvDescription.text = item.description
                initClickListener(root, item)
            }
        }

        private fun initClickListener(itemView: View, item: SubForumItem) {
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
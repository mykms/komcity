package ru.komcity.uicomponent.listselector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.komcity.uicomponent.databinding.UiSelectedCategoryViewBinding

/**
 * Created by Aleksei Kholoimov on 05.04.2020
 * <p>
 * Для показа в списке выбора (диалога)
 */
class ListSelectorAdapter(private val items: List<String>,
                          private val listener: (item: String, position: Int) -> Unit) : RecyclerView.Adapter<ListSelectorAdapter.ViewHolder>() {

    private var _binding: UiSelectedCategoryViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = UiSelectedCategoryViewBinding.inflate(LayoutInflater.from(parent.context), parent, true)
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.ui_list_selector_item, parent, false)
        return ViewHolder(binding.root)
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
                binding.tvTitle.text = item
                setOnClickListener { listener(item, position) }
            }
        }
    }
}
package ru.komcity.mobile.UI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.komcity.mobile.UI.Holder.BaseHolder

interface ItemClickListener<M> {
    fun onItemClick(item: M, position: Int)
}

/**
 * Базовый адаптер для списков
 */
abstract class BaseListAdapter<VH: BaseHolder<IL>, IL: Any>(items: List<IL>): RecyclerView.Adapter<VH>() {
    private val items: ArrayList<IL> = ArrayList()

    init {
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(setLayoutRes(), parent, false)
        return returnViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item: IL = items[position]
        val listener = View.OnClickListener {
            onItemClicked(item, position)
        }
        holder.setData(item)
        holder.setItemClickListener(listener)
    }

    fun getItems(): List<IL> {
        return items
    }

    protected fun getItemByPosition(position: Int): IL {
        return items[position]
    }

    fun clear() {
        items.clear()
    }

    open fun addItems(items: List<IL>) {
        this.items.addAll(items)
        notifyItemRangeInserted(this.items.size, items.size)
    }

    fun addItem(item: IL) {
        this.items.add(item)
        notifyItemInserted(this.items.size)
    }

    fun changeItem(position: Int, item: IL) {
        this.items[position] = item
        notifyItemChanged(position)
    }

    abstract fun setLayoutRes(): Int
    abstract fun returnViewHolder(view: View): VH
    abstract fun onItemClicked(item: IL, position: Int)
    abstract fun setClickListener(listener: ItemClickListener<IL>?)
}
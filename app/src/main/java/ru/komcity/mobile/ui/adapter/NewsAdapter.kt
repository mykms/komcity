package ru.komcity.mobile.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.komcity.mobile.viewModel.ImageLoader.ImageCropType
import ru.komcity.mobile.viewModel.ImageLoader.ImageLoader
import ru.komcity.mobile.viewModel.NewsItem
import ru.komcity.mobile.R
import ru.komcity.mobile.viewModel.AddNewsItem
import ru.komcity.mobile.viewModel.BaseHolderItem
import ru.komcity.mobile.viewModel.SearchNewsItem

class NewsAdapter(private val items: List<BaseHolderItem>, private val onItemClick: (item: NewsItem) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_search, parent, false)
                ViewHolderSearch(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_add, parent, false)
                ViewHolderAddNews(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
                ViewHolderNews(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is AddNewsItem -> 0
            is SearchNewsItem -> 1
            else -> 2
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as? ViewHolderSearch)?.bind(items[position] as SearchNewsItem)
            1 -> (holder as? ViewHolderAddNews)?.bind(items[position] as AddNewsItem)
            else -> (holder as? ViewHolderNews)?.setData(items[position])
        }
    }

    inner class ViewHolderSearch(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: SearchNewsItem) {
        }
    }

    inner class ViewHolderAddNews(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: AddNewsItem) {
        }
    }

    inner class ViewHolderNews(private val containerView: View) : RecyclerView.ViewHolder(containerView) {
        private val tvNewsDate = containerView.findViewById<TextView>(R.id.tvNewsDate)
        private val tvNewsTitle = containerView.findViewById<TextView>(R.id.tvNewsTitle)
        private val tvNewsShortText = containerView.findViewById<TextView>(R.id.tvNewsShortText)
        private val ivNews = containerView.findViewById<ImageView>(R.id.ivNews)

        fun setData(item: BaseHolderItem) {
            item as NewsItem
            tvNewsDate.text = item.date
            tvNewsTitle.text = item.title
            tvNewsShortText.text = item.shortText
            ImageLoader(item.previewImg, ivNews, ImageCropType.CROP_NO)
            initListeners(item)
        }

        private fun initListeners(item: NewsItem) {
            containerView.setOnClickListener { onItemClick(item) }
            tvNewsDate.setOnClickListener { onItemClick(item) }
            tvNewsTitle.setOnClickListener { onItemClick(item) }
            tvNewsShortText.setOnClickListener { onItemClick(item) }
            ivNews.setOnClickListener { onItemClick(item) }
        }
    }
}
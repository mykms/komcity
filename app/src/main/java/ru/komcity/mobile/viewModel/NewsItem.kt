package ru.komcity.mobile.viewModel

/**
 * ViewModel news
 */
data class NewsItem(val title: String,
                    val date: String,
                    val text: String,
                    val previewImg: String,
                    val imageUrls: List<String>,
                    val newsId: Int,
                    val forumId: Int): BaseHolderItem
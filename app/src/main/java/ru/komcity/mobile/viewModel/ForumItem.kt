package ru.komcity.mobile.viewModel

data class ForumItem(val forumName: String,
                     val description: String,
                     val countReplic: String,
                     val countTheme: String,
                     val linkForum: String)

data class SubForumItem(val forumName: String,
                        val description: String,
                        val countReplic: String,
                        val countTheme: String,
                        val linkForum: String)

data class ForumMessagesItem(val date: String,
                             val userName: String,
                             val message: String)
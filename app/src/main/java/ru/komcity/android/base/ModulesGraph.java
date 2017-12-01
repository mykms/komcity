package ru.komcity.android.base;

import ru.komcity.android.news.NewsFragment;

public class ModulesGraph {
    private final String nameNews = "newsFragment";
    private final String nameForum = "ForumFragment";

    private final String titleNews = "Новости";
    private final String titleForum = "Форум";

    private Class<?> newsClass = NewsFragment.class;
    private Class<?> forumClass = null;//ForumFragment.class;

    public ModulesGraph() {
        //
    }


    public String getNameNews() {
        return nameNews;
    }

    public String getNameForum() {
        return nameForum;
    }

    public String getTitleNews() {
        return titleNews;
    }

    public String getTitleForum() {
        return titleForum;
    }

    public Class<?> getNewsClass() {
        return newsClass;
    }

    public Class<?> getForumClass() {
        return forumClass;
    }

}

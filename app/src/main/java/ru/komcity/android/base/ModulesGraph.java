package ru.komcity.android.base;

import ru.komcity.android.announcement.AnnouncementFragment;
import ru.komcity.android.forum.ForumFragment;
import ru.komcity.android.news.NewsFragment;

public class ModulesGraph {
    private final String nameNews = "NewsFragment";
    private final String nameForum = "ForumFragment";
    private final String nameAnnouncement = "AnnouncementFragment";

    private final String titleNews = "Новости";
    private final String titleForum = "Форум";
    private final String titleAnnouncement = "Объявления";

    private Class<?> newsClass = NewsFragment.class;
    private Class<?> forumClass = ForumFragment.class;
    private Class<?> announcementClass = AnnouncementFragment.class;

    public ModulesGraph() {
        //
    }

    public String getNameNews() {
        return nameNews;
    }
    public String getNameForum() {
        return nameForum;
    }
    public String getNameAnnouncement() {
        return nameAnnouncement;
    }

    public String getTitleNews() {
        return titleNews;
    }
    public String getTitleForum() {
        return titleForum;
    }
    public String getTitleAnnouncement() {
        return titleAnnouncement;
    }
    public Class<?> getNewsClass() {
        return newsClass;
    }
    public Class<?> getForumClass() {
        return forumClass;
    }
    public Class<?> getAnnouncementClass() {
        return announcementClass;
    }
}

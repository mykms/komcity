package ru.komcity.android.base;

import ru.komcity.android.announcement.AnnouncementFragment;
import ru.komcity.android.forum.ForumFragment;
import ru.komcity.android.news.NewsFragment;
import ru.komcity.android.pricemap.MapPriceFragment;

public class ModulesGraph {
    private final String nameNews = "NewsFragment";
    private final String nameForum = "ForumFragment";
    private final String nameSubForum = "SubForumFragment";
    private final String nameAnnouncement = "AnnouncementFragment";
    private final String nameForumDetail = "ForumDetailFragment";
    private final String nameMapPriceMain = "MapPriceFragment";

    private final String titleNews = "Новости";
    private final String titleForum = "Форум";
    private final String titleSubForum = "Просмотр Форумов";
    private final String titleAnnouncement = "Объявления";
    private final String titleForumDetail = "Просмотр форума";
    private final String titleMapPriceMain = "Карта цен";

    private Class<?> newsClass = NewsFragment.class;
    private Class<?> forumClass = ForumFragment.class;
    private Class<?> announcementClass = AnnouncementFragment.class;
    private Class<?> mappriceClass = MapPriceFragment.class;

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
    public String getNameSubForum() {
        return nameSubForum;
    }
    public String getNameForumDetail() {
        return nameForumDetail;
    }
    public String getNameMapPriceMain() {
        return nameMapPriceMain;
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
    public String getTitleSubForum() {
        return titleSubForum;
    }
    public String getTitleForumDetail() {
        return titleForumDetail;
    }
    public String getTitleMapPriceMain() {
        return titleMapPriceMain;
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
    public Class<?> getMappriceClass() {
        return mappriceClass;
    }
}

package ru.komcity.mobile.base.AsyncLoader;

import java.util.List;

import ru.komcity.mobile.news.NewsItem;

public interface IHtmlLoader {
    void onReadyToShow(List<Object> items);
}

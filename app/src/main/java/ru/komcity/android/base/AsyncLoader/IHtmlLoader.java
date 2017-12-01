package ru.komcity.android.base.AsyncLoader;

import java.util.List;

import ru.komcity.android.news.NewsItem;

public interface IHtmlLoader {
    void onReadyToShow(List<Object> items);
}

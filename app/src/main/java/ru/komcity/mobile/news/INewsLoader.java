package ru.komcity.mobile.news;

import java.util.List;
import ru.komcity.mobile.base.AsyncLoader.IHtmlLoader;

public interface INewsLoader extends IHtmlLoader {
    void onLinksLoaded(List<NewsArchiveLinkItem> links);
}

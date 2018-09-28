package ru.komcity.mobile.base.AsyncLoader;

import org.jsoup.nodes.Document;

public interface IAsyncLoader {
    void onCompletedLoading(Document html);
}

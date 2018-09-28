package ru.komcity.mobile.announcement;

import java.util.List;
import ru.komcity.mobile.base.AsyncLoader.IHtmlLoader;

public interface IAnnoncementHtmlLoader extends IHtmlLoader {
    void onReadyToShowCategoryAndTypes(List<Object> items);
    void onReadyToShowSubCategory(List<Object> items);
}

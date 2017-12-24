package ru.komcity.android.announcement;

import java.util.List;
import ru.komcity.android.base.AsyncLoader.IHtmlLoader;

public interface IAnnoncementHtmlLoader extends IHtmlLoader {
    void onReadyToShowCategoryAndTypes(List<Object> items);
    void onReadyToShowSubCategory(List<Object> items);
}

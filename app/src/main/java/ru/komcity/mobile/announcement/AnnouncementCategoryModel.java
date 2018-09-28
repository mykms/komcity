package ru.komcity.mobile.announcement;

import java.util.List;

public class AnnouncementCategoryModel {
    private String title;
    private List<Object> subCategoryList;

    public AnnouncementCategoryModel(String title, List<Object> SubCategoryModelList) {
        this.title = title;
        this.subCategoryList = SubCategoryModelList;
    }

    public String getTitle() {
        return title;
    }
    public List<Object> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<Object> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}

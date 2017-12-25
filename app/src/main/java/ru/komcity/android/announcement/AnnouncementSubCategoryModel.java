package ru.komcity.android.announcement;

import java.util.List;

public class AnnouncementSubCategoryModel {
    private String catName;
    private List<AnnouncementSubCategoryItemModel> catNameItemsList;

    public AnnouncementSubCategoryModel(String catName, List<AnnouncementSubCategoryItemModel> catNameItemsList) {
        this.catName = catName;
        this.catNameItemsList = catNameItemsList;
    }

    public void setCatNameItems(List<AnnouncementSubCategoryItemModel> catNameItemsList) {
        this.catNameItemsList = catNameItemsList;
    }

    public String getCatName() {
        return catName;
    }

    public List<AnnouncementSubCategoryItemModel> getCatNameItemsList() {
        return catNameItemsList;
    }
}

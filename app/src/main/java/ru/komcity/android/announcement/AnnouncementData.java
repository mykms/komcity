package ru.komcity.android.announcement;

import java.util.ArrayList;
import java.util.List;
import ru.komcity.android.base.Utils;

public class AnnouncementData {
    private Utils utils = new Utils();
    private String[] titleArr;
    private String[] linkstitleSubCategoryArr;
    private String[] linksArr;
    private List<Object> AnnouncementList = new ArrayList<>();

    public AnnouncementData(String[] title,  String[] linkstitleSubCategory, String[] linksSubCategory) {
        if (title == null)
            titleArr = new String[0];
        else
            titleArr = title;

        if (linkstitleSubCategory == null)
            linkstitleSubCategoryArr = new String[0];
        else
            linkstitleSubCategoryArr = linkstitleSubCategory;

        if (linksSubCategory == null)
            linksArr = new String[0];
        else
            linksArr = linksSubCategory;
    }

    private void translateToModel() {
        if (titleArr.length > 0 && linksArr.length > 0) {
            for (int i = 0; i < titleArr.length; i++) {
                AnnouncementCategoryModel categoryModel = new AnnouncementCategoryModel(titleArr[i], null);
                List<Object> subCatList = new ArrayList<>();
                // получим все ссылки
                for (int j = 0; j < linksArr.length; j++) {
                    String StringForSpliting = linksArr[j];
                    if (StringForSpliting != null) {
                        if (!StringForSpliting.isEmpty()) {
                            try {
                                int limit = 3;
                                String[] refArr = StringForSpliting.split(", ", limit);
                                int pos = Integer.parseInt(refArr[0]);  // Соответствует номеру titleArr
                                if (pos == i) {
                                    subCatList.add(new AnnouncementTypeModel(linkstitleSubCategoryArr[j], refArr[0], refArr[1]));// Заполним корректно все элементы и добавим в список
                                    if (j == (linksArr.length - 1)) {
                                        categoryModel.setSubCategoryList(subCatList);
                                        AnnouncementList.add(categoryModel);
                                    }
                                } else if (pos > i || j == (linksArr.length - 1)) {
                                    categoryModel.setSubCategoryList(subCatList);
                                    AnnouncementList.add(categoryModel);
                                    break;
                                }
                            } catch (Exception ex) {
                                utils.getException(ex);
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Object> getModelList() {
        translateToModel();
        return AnnouncementList;
    }

    public AnnouncementCategoryModel getItem(int position) {
        return (AnnouncementCategoryModel)AnnouncementList.get(position);
    }
}

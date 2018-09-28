package ru.komcity.mobile.pricemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IProductDBListener {
    void onSaveProductResult(boolean result);
    void onProductTypesLoadComplete(HashMap<String, ArrayList<String>> items);
    void onProductLoadComplete(List<Object> items);
}

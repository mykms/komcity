package ru.komcity.android.pricemap;

public class PriceListItem {
    private PriceListModel priceModel;

    public PriceListItem() {
        //
    }

    public PriceListItem(PriceListModel model) {
        priceModel = model;
    }

    public PriceListModel getItem() {
        return priceModel;
    }

    public void setItem(PriceListModel model) {
        priceModel = model;
    }
}

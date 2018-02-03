package ru.komcity.android.pricemap;

import java.util.ArrayList;
import java.util.Calendar;

public class PriceListModel {
    public String dateadd;
    public ArrayList<Object> geo;
    public String marketAddress;
    public String marketName;
    public double price;
    public String prodName;
    public String prodType;
    public String prodSubType;
    public String user;

    public PriceListModel() {
        //
    }

    public PriceListModel(ArrayList<Object> geo, String marketAddress, String marketName, double price, String prodName, String prodType, String prodSubType, String user) {
        this.dateadd = Calendar.getInstance().getTime().toString();
        this.geo = geo;
        this.marketAddress = marketAddress;
        this.marketName = marketName;
        this.price = price;
        this.prodName = prodName;
        this.prodType = prodType;
        this.prodSubType = prodSubType;
        this.user = user;
    }
}

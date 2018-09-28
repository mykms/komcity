package ru.komcity.mobile.pricemap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PriceListModel {
    public String date;
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

    public PriceListModel(ArrayList<Object> geo,
                          String marketAddress,
                          String marketName,
                          double price,
                          String prodName,
                          String prodType,
                          String prodSubType,
                          String user) {

        this.date = Calendar.getInstance().getTime().toString();
        this.geo = geo;
        this.marketAddress = marketAddress;
        this.marketName = marketName;
        this.price = price;
        this.prodName = prodName;
        this.prodType = prodType;
        this.prodSubType = prodSubType;
        this.user = user;
    }

    public Date getDate() {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE MMM d HH:mm:ss zz yyyy", Locale.getDefault());

        Date stringDate = Calendar.getInstance().getTime();
        try {
            simpledateformat.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stringDate;
    }

    public String getProdName() {
        return prodName;
    }

    public String getMarketName() {
        return marketName;
    }

    public double getPrice() {
        return price;
    }
}

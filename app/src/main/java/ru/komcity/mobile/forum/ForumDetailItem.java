package ru.komcity.mobile.forum;

public class ForumDetailItem {
    private String Date;
    private String Name;
    private String Text;

    public ForumDetailItem(String date, String name, String text) {
        Date = date;
        Name = name;
        Text = text;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}

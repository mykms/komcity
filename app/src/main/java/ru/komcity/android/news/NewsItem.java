package ru.komcity.android.news;

public class NewsItem {
    private String title;
    private String date;
    private String text;
    private String thumbnail;

    public String getDate() {
        return date;
    }
    public String getText() {
        return text;
    }
    public String getTitle() {
        return title;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

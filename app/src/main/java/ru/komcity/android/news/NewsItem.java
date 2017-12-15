package ru.komcity.android.news;

public class NewsItem {
    private String title;   // Заголовок новости
    private String date;    // дата новости
    private String text;    // Содержимое новости
    private String image;   // Картинка для новости
    private String url;     // Ссылка но полную новсть

    public String getDate() {
        return date;
    }
    public String getText() {
        return text;
    }
    public String getTitle() {
        return title;
    }
    public String getImage() {
        return image;
    }
    public String getUrl() {
        return url;
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
    public void setImage(String image) {
        this.image = image;
    }
    public void setUrl(String mUrl) {
        this.url = mUrl;
    }
}

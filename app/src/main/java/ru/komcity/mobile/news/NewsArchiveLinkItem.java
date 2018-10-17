package ru.komcity.mobile.news;

public class NewsArchiveLinkItem {
    private String link;
    private int page;

    public NewsArchiveLinkItem() {
    }

    public NewsArchiveLinkItem(String link, int page) {
        this.link = link;
        this.page = page;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

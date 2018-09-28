package ru.komcity.mobile.announcement;

public class AnnouncementTypeModel {
    private String title;
    private String ref1;
    private String ref2;

    public AnnouncementTypeModel(String name, String ref1, String ref2) {
        this.title = name;
        this.ref1 = ref1;
        this.ref2 = ref2;
    }

    public String getTitle() {
        return this.title;
    }
    public String getRef1() {
        return ref1;
    }
    public String getRef2() {
        return ref2;
    }

}

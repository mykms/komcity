package ru.komcity.android.forum;

public class ForumItem {
    private String Title;
    private String Description;
    private String CountReplic;
    private String CountTheme;

    public ForumItem(String _title, String _discr, String _countReplic, String _countTheme)
    {
        Title = _title;
        Description = _discr;
        CountReplic = _countReplic;
        CountTheme = _countTheme;
    }

    public String getTitle()
    {
        return Title;
    }

    public String getDescription()
    {
        return Description;
    }

    public String getCountReplic()
    {
        return CountReplic;
    }

    public String getCountTheme() {
        return CountTheme;
    }
}

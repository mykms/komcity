package ru.komcity.android.forum;

public class ForumItem {
    private String Theme;
    private String Description;
    private String CountThemeReplic;

    public ForumItem(String _theme, String _discr, String _count)
    {
        Theme = _theme;
        Description = _discr;
        CountThemeReplic = _count;
    }

    public String getTheme()
    {
        return Theme;
    }

    public String getDescription()
    {
        return Description;
    }

    public String getCountThemeReplic()
    {
        return CountThemeReplic;
    }
}

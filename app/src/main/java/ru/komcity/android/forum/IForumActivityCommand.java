package ru.komcity.android.forum;

import ru.komcity.android.base.IMainActivityCommand;

public interface IForumActivityCommand extends IMainActivityCommand {
    void replaceFragment(String fragmentName, ForumItem item);
}

package ru.komcity.mobile.forum;

import ru.komcity.mobile.base.IMainActivityCommand;

public interface IForumActivityCommand extends IMainActivityCommand {
    void replaceFragment(String fragmentName, ForumItem item);
}

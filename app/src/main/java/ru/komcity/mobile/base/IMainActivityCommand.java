package ru.komcity.mobile.base;

import android.app.Fragment;
import android.os.Bundle;

public interface IMainActivityCommand {
    void onSetTitle(String mTitle);
    void setVisibleMenuIcon(boolean isVisible);
    void setCurrentFragment(Fragment fragment);
    void onLoadFragment(String fragmentName, Bundle args);
    void onError(String message);
}
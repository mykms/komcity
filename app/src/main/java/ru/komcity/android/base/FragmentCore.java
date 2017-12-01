package ru.komcity.android.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import ru.komcity.android.R;
import ru.komcity.android.forum.ForumFragment;
import ru.komcity.android.news.NewsFragment;

public class FragmentCore {

    private Activity activity = null;
    private FragmentManager fragmentManager = null;
    public FragmentCore(Activity fragmentActivity, FragmentManager mFragTrans) {
        activity = fragmentActivity;
        fragmentManager = mFragTrans;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    public Fragment findFragment(String fragmentTAG) {
        Fragment fragment = null;
        fragment = fragmentManager.findFragmentByTag(fragmentTAG);
        ModulesGraph modules = new ModulesGraph();
        if (fragment == null) {
            if (fragmentTAG.equals(modules.getNameNews())) {
                fragment = new NewsFragment();  // Новости
            } else if (fragmentTAG.equals(modules.getNameForum())) {
                fragment = new ForumFragment();  // Форум
            } else {
                fragment = new Fragment();
            }

        }
        if (fragment != null) {
            replaceFragment(fragment);
        }
        return fragment;
    }
}

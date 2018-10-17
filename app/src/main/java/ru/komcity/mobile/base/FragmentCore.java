package ru.komcity.mobile.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ru.komcity.mobile.R;
import ru.komcity.mobile.announcement.AnnouncementFragment;
import ru.komcity.mobile.forum.ForumDetailFragment;
import ru.komcity.mobile.forum.ForumFragment;
import ru.komcity.mobile.forum.SubForumFragment;
import ru.komcity.mobile.news.NewsFragment;
import ru.komcity.mobile.pricemap.MapPriceFragment;

public class FragmentCore {
    private Bundle bundle = null;
    private FragmentManager fragmentManager = null;
    private Utils utils = new Utils();

    public FragmentCore(FragmentManager mFragTrans) {
        fragmentManager = mFragTrans;
    }

    private void replaceFragment(Fragment fragment, @IdRes int ResID) {
        @IdRes int contentResourse = R.id.content_frame;
        if (ResID != 0) {
            contentResourse = ResID;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(contentResourse, fragment);
        fragmentTransaction.commit();
    }

    public void setDataForFragment(HashMap hashMap, String[] typeListForhashMap) {
        bundle = new Bundle();
        int i = 0;
        for (Object entryObj : hashMap.entrySet()) {
            try {
                Map.Entry entry = (Map.Entry) entryObj;
                if (typeListForhashMap[i].equals("int"))
                    bundle.putInt(entry.getKey().toString(), (int) entry.getValue());
                if (typeListForhashMap[i].equals("long"))
                    bundle.putLong(entry.getKey().toString(), (long) entry.getValue());
                if (typeListForhashMap[i].equals("String"))
                    bundle.putString(entry.getKey().toString(), entry.getValue().toString());
                if (typeListForhashMap[i].equals("ArrayList"))
                    bundle.putStringArrayList(entry.getKey().toString(), (ArrayList<String>) entry.getValue());
            } catch (Exception ex) {
                utils.getException(ex);
            }
            i++;
        }
    }

    /**
     * Ищет фрагмент по тэгу, если находит, то загружает его в слой ResID
     *
     * @param fragmentTAG название тэга
     * @param ResID       Ссылка на id FrameLayout
     * @return Возвращает найденный фрагмент
     */
    public Fragment findFragment(String fragmentTAG, @IdRes int ResID) {
        return findFragment(fragmentTAG, ResID, null);
    }

    public Fragment findFragment(String fragmentTAG, @IdRes int ResID, @Nullable Bundle args) {
        Fragment fragment = null;
        fragment = fragmentManager.findFragmentByTag(fragmentTAG);
        ModulesGraph modules = new ModulesGraph();
        if (fragment == null) {
            if (fragmentTAG.equals(modules.getNameNews())) {
                fragment = new NewsFragment();              // Новости
            } else if (fragmentTAG.equals(modules.getNameForum())) {
                fragment = new ForumFragment();             // Форум
            } else if (fragmentTAG.equals(modules.getNameAnnouncement())) {
                fragment = new AnnouncementFragment();      // Объявления
            } else if (fragmentTAG.equals(modules.getNameSubForum())) {
                fragment = new SubForumFragment();          // ПодФорумы
            } else if (fragmentTAG.equals(modules.getNameForumDetail())) {
                fragment = new ForumDetailFragment();       // Форум детально (посты)
            } else if (fragmentTAG.equals(modules.getNameMapPriceMain())) {
                fragment = new MapPriceFragment();         // Карта цен
            } else {
                fragment = new Fragment();
            }
        }

        if (args != null) {
            fragment.setArguments(args);
        }
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        replaceFragment(fragment, ResID);
        return fragment;
    }
}

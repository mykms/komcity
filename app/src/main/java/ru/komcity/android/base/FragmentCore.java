package ru.komcity.android.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ru.komcity.android.R;
import ru.komcity.android.announcement.AnnouncementFragment;
import ru.komcity.android.forum.ForumDetailFragment;
import ru.komcity.android.forum.ForumFragment;
import ru.komcity.android.forum.SubForumFragment;
import ru.komcity.android.news.NewsFragment;
import ru.komcity.android.pricemap.MapPriceFragment;

public class FragmentCore {
    private Bundle bundle = null;
    private FragmentManager fragmentManager = null;
    private Utils utils = new Utils();

    public FragmentCore(FragmentManager mFragTrans) {
        fragmentManager = mFragTrans;
    }

    public void replaceFragment(Fragment fragment, @IdRes int ResID) {
        @IdRes int contentResourse = R.id.content_frame;
        if (ResID != 0) {
            contentResourse = ResID;
        }

        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
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
                fragment = new SubForumFragment();                  // ПодФорумы
            } else if (fragmentTAG.equals(modules.getNameForumDetail())) {
                fragment = new ForumDetailFragment();       // Форум детально (посты)
            } else if (fragmentTAG.equals(modules.getNameMapPriceMain())) {
                fragment = new MapPriceFragment();         // Карта цен
            } else {
                fragment = new Fragment();
            }
        }

        if (fragment != null) {
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            replaceFragment(fragment, ResID);
        }
        return fragment;
    }
}

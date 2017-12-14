package ru.komcity.android;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.base.FragmentCore;
import ru.komcity.android.base.IMainActivityCommand;
import ru.komcity.android.base.ModulesGraph;
import ru.komcity.android.news.NewsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IMainActivityCommand {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    private ModulesGraph modules = new ModulesGraph();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                                                         this,
                                                                drawer,
                                                                toolbar,
                                                                R.string.navigation_drawer_open,
                                                                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();
        showFragment(this, fragmentManager, modules.getNameNews()); // По умолчанию показываем новости
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            showFragment(this, fragmentManager, modules.getNameNews());
        } else if (id == R.id.nav_gallery) {
            showFragment(this, fragmentManager, modules.getNameForum());
        } else if (id == R.id.nav_slideshow) {
            showFragment(this, fragmentManager, modules.getNameAnnouncement());
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Activity mActivity, FragmentManager mFrManager, String fragmentTAG) {
        FragmentCore fragmentEngine = new FragmentCore(mFrManager);
        fragmentEngine.findFragment(fragmentTAG);
    }

    @Override
    public void onSetTitle(String mTitle) {
        if (mTitle != null)
            if (!mTitle.isEmpty())
                if (toolbar != null)
                    toolbar.setTitle(mTitle);
    }
}

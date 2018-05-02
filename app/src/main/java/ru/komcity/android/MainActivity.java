package ru.komcity.android;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.base.FragmentCore;
import ru.komcity.android.base.IMainActivityCommand;
import ru.komcity.android.base.ModulesGraph;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IMainActivityCommand {
    private ModulesGraph modules = new ModulesGraph();
    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle toggle = null;

    @BindView(R.id.toolbar)       Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view)      NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Доступ к верхней шапке
        View navigationHeader = navigationView.getHeaderView(0);
        if (navigationHeader != null) {
            TextView tvMailSend = navigationHeader.findViewById(R.id.tv_navbar_mail);
            tvMailSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mailSendIntentCreate();
                }
            });
        }

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                                                                R.string.navigation_drawer_open,
                                                                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();
        showFragment(fragmentManager, modules.getNameNews()); // По умолчанию показываем новости
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.removeDrawerListener(toggle);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_news) {
            showFragment(fragmentManager, modules.getNameNews());
        } else if (id == R.id.nav_forum) {
            showFragment(fragmentManager, modules.getNameForum());
        } else if (id == R.id.nav_announcement) {
            showFragment(fragmentManager, modules.getNameAnnouncement());
        } else if (id == R.id.nav_map_price) {
            showFragment(fragmentManager, modules.getNameMapPriceMain());
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(FragmentManager mFrManager, String fragmentTAG) {
        FragmentCore fragmentEngine = new FragmentCore(mFrManager);
        fragmentEngine.findFragment(fragmentTAG, 0);
    }

    private void mailSendIntentCreate() {
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setAction(Intent.ACTION_SEND);
        mailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mailIntent.setType("plain/text");
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { getString(R.string.email_for_send) });
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение из мобильного приложения");
        startActivity(Intent.createChooser(mailIntent, "Написать в"));
    }

    @Override
    public void onSetTitle(String mTitle) {
        if (mTitle != null)
            if (!mTitle.isEmpty())
                if (toolbar != null)
                    toolbar.setTitle(mTitle);
    }
}

package ru.komcity.mobile;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.mobile.base.FragmentCore;
import ru.komcity.mobile.base.IMainActivityCommand;
import ru.komcity.mobile.base.ModulesGraph;
import ru.komcity.mobile.base.RequestCodes;
import ru.komcity.mobile.base.Utils;
import ru.komcity.mobile.news.CalendarClickListener;
import ru.komcity.mobile.pricemap.MapPriceFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IMainActivityCommand {
    private ModulesGraph modules = new ModulesGraph();
    private FragmentManager fragmentManager;
    private ActionBarDrawerToggle toggle = null;
    private Utils utils = new Utils();
    private boolean isVisibleMenuIcon = true;
    private CalendarClickListener calendarClickListener = null;
    private FragmentBaseListener fragmentBaseListener = null;

    @BindView(R.id.toolbar)       Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view)      NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        utils = new Utils(getApplicationContext());

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
    protected void onResume() {
        super.onResume();
        if (this.fragmentBaseListener != null) {
            this.fragmentBaseListener.getToolbarTitle();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.isVisibleMenuIcon) {
            getMenuInflater().inflate(R.menu.news_calendar, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.news_calendar:
                showCalendar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        } else if (id == R.id.nav_exit) {
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(FragmentManager mFrManager, String fragmentTAG) {
        FragmentCore fragmentEngine = new FragmentCore(mFrManager);
        fragmentEngine.findFragment(fragmentTAG, 0);
    }

    private void showFragment(FragmentManager mFrManager, String fragmentTAG, Bundle args) {
        FragmentCore fragmentEngine = new FragmentCore(mFrManager);
        fragmentEngine.findFragment(fragmentTAG, 0, args);
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

    private void showCalendar() {
        Calendar curCalendar = Calendar.getInstance();
        DatePickerDialog dpDialog = new DatePickerDialog(this, R.style.DateCalendar, getDateListener(),
                curCalendar.get(Calendar.YEAR),
                curCalendar.get(Calendar.MONTH), curCalendar.get(Calendar.DAY_OF_MONTH));
        dpDialog.setTitle("За какую дату вы хотите просмотреть новость?");
        dpDialog.show();
    }

    private DatePickerDialog.OnDateSetListener getDateListener() {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,  int month, int dayOfMonth) {
                if (calendarClickListener != null) {
                    Calendar selCalendar = Calendar.getInstance();
                    selCalendar.set(year, month, dayOfMonth);
                    calendarClickListener.onDateSelected(selCalendar.getTimeInMillis());
                }
            }
        };
    }

    @Override
    public void onSetTitle(String mTitle) {
        if (mTitle == null) {
            mTitle = "";
        }
        if (!mTitle.isEmpty() && toolbar != null) {
            toolbar.setTitle(mTitle);
        }
    }

    @Override
    public void setVisibleMenuIcon(boolean isVisible) {
        this.isVisibleMenuIcon = isVisible;
    }

    @Override
    public void setCurrentFragment(Fragment fragment) {
        if (fragment instanceof CalendarClickListener) {
            this.calendarClickListener = (CalendarClickListener) fragment;
        }
        if (fragment instanceof FragmentBaseListener) {
            this.fragmentBaseListener = (FragmentBaseListener) fragment;
        }
    }

    @Override
    public void onLoadFragment(String fragmentName, Bundle args) {
        if (fragmentName == null) {
            fragmentName = "";
        }
        if (!fragmentName.isEmpty()) {
            showFragment(fragmentManager, modules.getNameNews(), args);
        }
    }

    @Override
    public void onError(String message) {
        if (message == null) {
            message = "";
        }
        if (!message.isEmpty()) {
            Snackbar.make(this.getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCodes.LOCATION: {
                checkLocation(grantResults);
                break;
            }
            case RequestCodes.PHONE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                } else {
                    utils.showMessage(getString(R.string.msg_access_deny_location), true);
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void checkLocation(@NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                MapPriceFragment curFragment = (MapPriceFragment) fragmentManager.findFragmentById(R.id.content_frame);
                curFragment.setInitInfoForMapFragment();
            } catch (Exception ex) {
                utils.getException(ex);
            }
        } else {
            utils.showMessage(getString(R.string.msg_access_deny_location), true);
        }
    }
}

package testapp.komcity.ru.myapplicationetalon;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MvpActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.drawer_layout) public DrawerLayout drawer;
    @BindView(R.id.nav_view) public NavigationView navigationView;

    @InjectPresenter
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNetworkActive()
    {
        ConnectivityManager connetInet = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = connetInet.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && connetInet.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    @Override
    public void isProgressing(boolean isProgress) {
        //
    }
}

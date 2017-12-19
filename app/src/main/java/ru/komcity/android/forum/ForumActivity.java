package ru.komcity.android.forum;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.komcity.android.R;
import ru.komcity.android.base.FragmentCore;
import ru.komcity.android.base.ModulesGraph;

public class ForumActivity extends AppCompatActivity implements IForumActivityCommand {
    @BindView(R.id.toolbar_top) Toolbar toolbar;
    private FragmentCore fragmentEngine = null;
    private ModulesGraph modules = new ModulesGraph();
    private HashMap<String, String> data = new HashMap<>();
    private String[] typesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
            ab.setTitle("Просмотр форумов");
        }

        fragmentEngine = new FragmentCore(getFragmentManager());

        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra("URL");
            String fName = intent.getStringExtra("NAME");

            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeButtonEnabled(true);
                if (fName != null)
                    ab.setTitle(fName);
                else
                    ab.setTitle("Просмотр форумов");
            }
            data.put("NAME", fName);
            data.put("URL", url);
            typesList = new String[data.size()];
            typesList[0] = "String";
            typesList[1] = "String";
        }

        loadSubForum();
    }

    private void loadSubForum() {
        fragmentEngine.setDataForFragment(data, typesList);
        fragmentEngine.findFragment(modules.getNameSubForum(), R.id.forum_content_frame);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSetTitle(String mTitle) {
        if (mTitle != null)
            if (!mTitle.isEmpty())
                if (toolbar != null)
                    toolbar.setTitle(mTitle);
    }

    @Override
    public void replaceFragment(String fragmentName) {
        if (fragmentName.equals(modules.getNameForumDetail())) {
            fragmentEngine.findFragment(modules.getNameForumDetail(), R.id.forum_content_frame);
        }
    }
}

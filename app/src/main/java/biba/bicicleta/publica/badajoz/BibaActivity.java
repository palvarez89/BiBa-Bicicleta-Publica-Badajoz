package biba.bicicleta.publica.badajoz;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import biba.bicicleta.publica.badajoz.fragments.ListaEstaciones;
import biba.bicicleta.publica.badajoz.fragments.Map;
import biba.bicicleta.publica.badajoz.utils.Analytics;
import biba.bicicleta.publica.badajoz.utils.EstacionList;
import biba.bicicleta.publica.badajoz.utils.StationsRequest;


public class BibaActivity extends ActionBarActivity {

    Analytics analytics;
    Fragment mainFragment;
    ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;

    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);


    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    private void performRequest() {

        StationsRequest request = new StationsRequest();

        spiceManager.execute(request, "cache", DurationInMillis.ONE_MINUTE, new ListFollowersRequestListener());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biba_main);

        analytics = new Analytics(this);
        analytics.screenView(this.getClass().getSimpleName());

        performRequest();
        initToolbar();
        initFragment(savedInstanceState);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        String[] drawerList = getResources().getStringArray(R.array.drawer_menu_list);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, drawerList));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    private void initFragment(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            selectItem(0);
        } else {
            if (getSupportFragmentManager().findFragmentByTag("map_fragment") != null) {
                mainFragment = getSupportFragmentManager().findFragmentByTag("map_fragment");
            }
            else if (getSupportFragmentManager().findFragmentByTag("list_fragment") != null) {
                mainFragment = getSupportFragmentManager().findFragmentByTag("list_fragment");
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        String tag = "void";
        switch (position) {
            case 0:
                mainFragment = new ListaEstaciones();
                performRequest();
                tag = "list_fragment";
                break;
            case 1:
                mainFragment = new Map();
                tag = "map_fragment";
                break;
        }


        mainFragment.setArguments(getIntent().getExtras());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.listaestaciones_f_container, mainFragment, tag)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    //inner class of your spiced Activity
    private class ListFollowersRequestListener implements RequestListener<EstacionList> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.w("ROBOSPICE", "FAILURE");
        }

        @Override
        public void onRequestSuccess(EstacionList listFollowers) {
            Log.w("ROBOSPICE", "SUCCESS");
            //update your UI
        }
    }
}

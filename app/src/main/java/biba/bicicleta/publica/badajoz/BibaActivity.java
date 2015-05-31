package biba.bicicleta.publica.badajoz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import biba.bicicleta.publica.badajoz.fragments.ListaEstaciones;
import biba.bicicleta.publica.badajoz.fragments.ListaEstacionesFavs;
import biba.bicicleta.publica.badajoz.fragments.Map;
import biba.bicicleta.publica.badajoz.utils.Analytics;
import biba.bicicleta.publica.badajoz.utils.AppDonate;
import biba.bicicleta.publica.badajoz.utils.AppRater;


public class BibaActivity extends AppCompatActivity {

    Analytics analytics;
    Fragment mainFragment;
    ActionBarDrawerToggle mDrawerToggle;
    ListView mDrawerList;
    DrawerLayout mDrawerLayout;

    private final static String APP_DONATE_PACKAGE_NAME = "biba.bicicleta.publica.badajoz.donate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biba_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            analytics = new Analytics(this);
            analytics.screenView(this.getClass().getSimpleName());
        }

        initToolbar();
        initFragment(savedInstanceState);
        AppRater.app_launched(this);
        AppDonate.app_launched(this);
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
            } else if (getSupportFragmentManager().findFragmentByTag("list_fragment") != null) {
                mainFragment = getSupportFragmentManager().findFragmentByTag("list_fragment");
            } else if (getSupportFragmentManager().findFragmentByTag("list_fragment_favourites") != null) {
                mainFragment = getSupportFragmentManager().findFragmentByTag("list_fragment_favourites");
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
                tag = "list_fragment";
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    mainFragment = new Map();
                    tag = "map_fragment";
                    break;
                }
                else {
                    mDrawerLayout.closeDrawer(mDrawerList);
                    versionNotCompatibleDialog(this);
                    return;
                }
            case 2:
                mainFragment = new ListaEstacionesFavs();
                tag = "list_fragment_favourites";
                break;
            case 3:
                openCallIncident(this);
                return;
            case 4:
                openDonateVersion(this);
                return;
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

    public static void openDonateVersion(Context context) {
        context.startActivity(new Intent(
                Intent.ACTION_VIEW, Uri
                .parse("market://details?id="
                        + APP_DONATE_PACKAGE_NAME)));
    }

    public static void openCallIncident(Context context) {
        String url = "tel:666500114";
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse(url));
            context.startActivity(intent);
        }
    }

    public void versionNotCompatibleDialog (Context context) {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.versionNotCompatible))
                .setTitle(context.getString(R.string.compatibilityError))
                .setPositiveButton(context.getString(R.string.understood),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                dialog.dismiss();
                            }
                        });
        dialog = builder.create();
        dialog.show();

    }

}

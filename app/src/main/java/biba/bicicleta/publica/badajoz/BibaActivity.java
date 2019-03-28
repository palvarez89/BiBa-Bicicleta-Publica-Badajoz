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
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import biba.bicicleta.publica.badajoz.adapters.DrawerAdapter;
import biba.bicicleta.publica.badajoz.fragments.ListaEstaciones;
import biba.bicicleta.publica.badajoz.fragments.ListaEstacionesFavs;
import biba.bicicleta.publica.badajoz.fragments.Map;
import biba.bicicleta.publica.badajoz.utils.AppDonate;
import biba.bicicleta.publica.badajoz.utils.AppRater;


public class BibaActivity extends AppCompatActivity {

    private Fragment mainFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private RecyclerView mDrawerRecycler;
    private DrawerAdapter mAdapter;
    private final int[] ICONS = new int[]{
            R.drawable.ic_list,
            R.drawable.ic_map,
            R.drawable.ic_star,
            R.drawable.ic_call,
            R.drawable.ic_donate
    };

    private final static String APP_DONATE_PACKAGE_NAME = "biba.bicicleta.publica.badajoz.donate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biba_main);

        initToolbar();
        initFragment(savedInstanceState);
        AppRater.app_launched(this);
        AppDonate.app_launched(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout dp;
//        dp = (LinearLayout) findViewById(R.id.drawer_parent);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerToggle.syncState();

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerRecycler = (RecyclerView) findViewById(R.id.left_drawer);
        mDrawerRecycler.setHasFixedSize(true);

        int PROFILE = R.mipmap.ic_launcher;
        String NAME = "BiBa";
        String TEXT = "Badajoz, mejor en bici!";

        mAdapter = new DrawerAdapter(getResources().getStringArray(R.array.drawer_menu_list),
                ICONS, NAME, TEXT, PROFILE);
        mDrawerRecycler.setAdapter(mAdapter);
        mDrawerRecycler.setLayoutManager(new LinearLayoutManager(this));

        final GestureDetector mGestureDetector = new GestureDetector(BibaActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mDrawerRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    mDrawerLayout.closeDrawer(mDrawerRecycler);
                    selectItem(recyclerView.getChildLayoutPosition(child) -1);
                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

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

    private void selectItem(int position) {
        String tag;
        switch (position) {
            case 0:
                mainFragment = new ListaEstaciones();
                tag = "list_fragment";
                break;
            case 1:
                mainFragment = new Map();
                tag = "map_fragment";
                break;
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
            default:
                return;
        }


        mainFragment.setArguments(getIntent().getExtras());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.listaestaciones_f_container, mainFragment, tag)
                .commit();

        mAdapter.setSelected(position);
    }

    public static void openDonateVersion(final Context context) {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

        String message = context.getString(R.string.DonateDialog);
        builder.setMessage(message)
                .setTitle(context.getString(R.string.DonateTitle))
                .setIcon(context.getApplicationInfo().icon)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.DonateNow),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                context.startActivity(new Intent(
                                        Intent.ACTION_VIEW, Uri
                                        .parse("market://details?id="
                                                + APP_DONATE_PACKAGE_NAME)));
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(context.getString(R.string.NoThanks),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }
                        });
        dialog = builder.create();
        dialog.show();
    }

    public static void openCallIncident(Context context) {
        String url = "tel:666500114";
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse(url));
            context.startActivity(intent);
        }
    }
}

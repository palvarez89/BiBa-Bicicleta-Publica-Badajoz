package biba.bicicleta.publica.badajoz;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Vector;

import biba.bicicleta.publica.badajoz.R;

//import biba.bicicleta.publica.badajoz.fragments.ActivityCommunicator;
//import biba.bicicleta.publica.badajoz.fragments.ListaEstaciones;


public class MainActivity extends Activity {

    Vector<Estacion> estaciones;
    InfoEstaciones infoEstaciones;

    LoadJson asyncTask;

    Activity activity;

    MiAdaptador adaptador;

    String deb = "DEBUG";
//    Fragment listFragment = null;
    boolean enFavs = false;

    private GeneralSwipeRefreshLayout mSwipeRefreshLayout;
//    private RecyclerView mRecyclerView;
//    private CatNamesRecyclerViewAdapter mCatNamesRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        estaciones = new Vector<Estacion>();
        infoEstaciones = InfoEstaciones.getInstance();

        final ListView listView;
        listView = (ListView) findViewById(R.id.list);

        new LoadJson(activity, listView).execute();

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
//
//        // Assign adapter to ListView
        listView.setAdapter(adapter);


        /**************** Create Custom Adapter *********/

//        listView.setBackgroundColor(0xFF888388);

//        listFragment = new ListaEstaciones(enFavs);
//
//        listFragment.setRetainInstance(true);
//        FragmentTransaction transList = getSupportFragmentManager().beginTransaction();
//        transList.add(R.id.listaestaciones_f_container, listFragment);
//        transList.commit();

        mSwipeRefreshLayout = (GeneralSwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

//        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerview);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(layoutManager);
//        setupAdapter();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        mSwipeRefreshLayout.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
            @Override
            public boolean canChildScrollUp() {
                return listView.getFirstVisiblePosition() > 0 ||
                        listView.getChildAt(0) == null ||
                        listView.getChildAt(0).getTop() < 0;
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new GeneralSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        setupAdapter();
//                        update();

//                        This is not possible, use AsyncTask
//                        try {
//                            wait(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        new LoadJson(activity, listView).execute();

                    }
                    // TODO: this should wait until refresh
                }, 2);
            }
        });
    }

    public void passDataToActivity(Vector<Estacion> estaciones) {
        Log.w(deb, "BiBa Activity passDataToActivity");
//        if (map != null && estaciones != null) {
//
//            final Vector<Estacion> stations = estaciones;
//            Log.w(deb, "BiBa Activity hayMapa");
//
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    map.clear();
//                    for (int i = 0; i < stations.size(); i++) {
//
//                        showPoint(stations.get(i));
//
//                    }
//
//                }
//            });
//        }

    }

    public void update() {

        // if (listFragment == null) {
        // listFragment = new ListaEstaciones();
        // // ((ListaEstaciones) listFragment).newInstance();
        //
        // listFragment.setRetainInstance(true);
        // }

//        ((ListaEstaciones) listFragment).update();
        // FragmentTransaction transaction = getSupportFragmentManager()
        // .beginTransaction();
        // transaction.detach(listFragment);
        // transaction.attach(listFragment);
        // transaction.commit();
        Log.w(deb, "BiBa Activity update ");
    }


    public class LoadJson extends AsyncTask<Void, Integer, Vector<Estacion>> {

        Activity activity;
        ListView listView;
        GeneralSwipeRefreshLayout swipeLayout;

        public LoadJson(Activity activity, ListView listView){
            this.activity = activity;
            this.listView = listView;
            swipeLayout = (GeneralSwipeRefreshLayout) activity.findViewById(R.id.activity_main_swipe_refresh_layout);

        }

        @Override
        protected Vector<Estacion> doInBackground(Void... params) {
            Log.w(deb, "BiBa Activity doinbackround");
            try {
                estaciones = new Vector<Estacion>();
                estaciones = infoEstaciones.getInfo(true); // True to force refresh
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return estaciones;
        }

        @Override
        protected void onPostExecute(Vector<Estacion> result) {



            adaptador = new MiAdaptador(activity, result);
            listView.setAdapter(adaptador);
            adaptador.notifyDataSetChanged();


            swipeLayout.setRefreshing(false);

        }
    }


    public interface AsyncResponse {
        void updateList(Vector<Estacion> result);
    }
}



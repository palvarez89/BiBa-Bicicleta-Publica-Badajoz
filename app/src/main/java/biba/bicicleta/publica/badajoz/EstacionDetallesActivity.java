package biba.bicicleta.publica.badajoz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import biba.bicicleta.publica.badajoz.adapters.EstacionDetallesAdapter;
import biba.bicicleta.publica.badajoz.adapters.ListaEstacionesAdapter;
import biba.bicicleta.publica.badajoz.objects.Estacion;
import biba.bicicleta.publica.badajoz.objects.MessageList;
import biba.bicicleta.publica.badajoz.utils.CommentsRequest;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;
import biba.bicicleta.publica.badajoz.views.EstacionViewHolder;

public class EstacionDetallesActivity extends AppCompatActivity {

    RecyclerView messagesRecycler;
    private GeneralSwipeRefreshLayout swipeLayout;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);
    private int stationNumber;
    private CardView topCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Estacion e = (Estacion) getIntent().getParcelableExtra("estacion");
        stationNumber = e.getN();

        setContentView(R.layout.activity_estacion_detalles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initSwipeLayout();
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        initTopCard(e);


        messagesRecycler = (RecyclerView) findViewById(R.id.messageList);
        messagesRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        messagesRecycler.setLayoutManager(llm);
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(this);
        performRequest(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeLayout != null) {
            swipeLayout.setRefreshing(false);
            swipeLayout.destroyDrawingCache();
            swipeLayout.clearAnimation();
        }
    }

    @Override
    public void onStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }

    private void initTopCard(Estacion e) {
        topCardView = (CardView) findViewById(R.id.top_card_detalles);

        topCardView.setRadius(0);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)topCardView.getLayoutParams();
        params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
        topCardView.setLayoutParams(params);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
        topCardView.setCardElevation(px);

        final EstacionViewHolder evh = EstacionViewHolder.newInstance(topCardView);
        final EstacionViewHolder evhFinal = evh;
        int numero = e.getN();
        String nombre = toLowerCase(e.getName());
        int bicis = e.getAvail();
        int parkings = e.getSpace();
        boolean estado = e.getStateBool();
        evh.setEstacionInfo(numero, nombre, bicis, parkings, estado);

        final SharedPreferences prefs;
        prefs = getSharedPreferences(
                "biba.bicicleta.publica.badajoz", Context.MODE_PRIVATE);
        final int realPosition = numero -1;
        final boolean isFav = prefs.getBoolean("fav" + realPosition, false);

        evh.setFavStar(isFav);

        evh.mFavStar.setOnClickListener(new CardClickListener(isFav) {
            @Override
            public void onClick(View v) {
                isFav = !isFav;
                evhFinal.setFavStar(isFav);
                prefs.edit().putBoolean("fav" + realPosition, isFav).commit();

            }
        });
    }
    private void initSwipeLayout() {
        swipeLayout = (GeneralSwipeRefreshLayout) findViewById(
                R.id.activity_estacion_detalles_swipe_refresh_layout);

        // Setup swipeLayout colors
        swipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        // Setup swipeLayout to play nice with thie ListView
        swipeLayout.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
            @Override
            public boolean canChildScrollUp() {
                LinearLayoutManager layoutManager = ((LinearLayoutManager) messagesRecycler.getLayoutManager());
                int firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                return firstVisiblePosition > 0;
            }
        });

        swipeLayout.setOnRefreshListener(new GeneralSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.w("EstacionDetallesAct", "puuuuuuuled");
                        performRequest(true);
                    }
                });
            }
        });
    }

    private void performRequest(boolean force) {
//        if (!force && bibaApp.estaciones != null) {
//            updateList(bibaApp.estaciones);
//            return;
//        }

        CommentsRequest request = new CommentsRequest(stationNumber);
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
        spiceManager.execute(request, "comments-cache" + stationNumber, DurationInMillis.ONE_SECOND, new MessageListRequestListener());
    }

    private class MessageListRequestListener implements RequestListener<MessageList> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), R.string.failed_update,
                    Toast.LENGTH_LONG).show();
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                }
            });
        }

        @Override
        public void onRequestSuccess(MessageList messageList) {
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                }
            });
            EstacionDetallesAdapter eda = new EstacionDetallesAdapter(messageList);
            messagesRecycler.setAdapter(eda);
        }
    }



    private String toLowerCase(String str) {
        String[] words = str.split("\\s");
        String out = "";

        for (int i = 0; i < words.length - 1; i++) {
            out = out + toLowerCaseWord(words[i]) + " ";
        }
        out = out + toLowerCaseWord(words[words.length - 1]);
        return out;
    }

    private String toLowerCaseWord(String str) {

        if (str.length() == 0) return "";

        if (str.length() == 1) return str.toUpperCase();

        if (!Character.isLetter(str.charAt(0))) {
            return str.substring(0, 1) + str.substring(1, 2).toUpperCase() + str.substring(2).toLowerCase();
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }

    public abstract class CardClickListener implements View.OnClickListener {
        boolean isFav;

        public CardClickListener(boolean isFav) {
            this.isFav = isFav;
        }
    }

}

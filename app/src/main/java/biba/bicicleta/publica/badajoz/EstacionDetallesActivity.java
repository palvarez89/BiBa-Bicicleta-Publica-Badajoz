package biba.bicicleta.publica.badajoz;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import biba.bicicleta.publica.badajoz.adapters.EstacionDetallesAdapter;
import biba.bicicleta.publica.badajoz.objects.MessageList;
import biba.bicicleta.publica.badajoz.utils.CommentsRequest;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;

public class EstacionDetallesActivity extends AppCompatActivity {

    RecyclerView messagesRecycler;
    private GeneralSwipeRefreshLayout swipeLayout;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);
    private int stationNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stationNumber = Integer.valueOf(getIntent().getExtras().getString("estacion"));

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
}

package biba.bicicleta.publica.badajoz;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import biba.bicicleta.publica.badajoz.adapters.EstacionDetallesAdapter;
import biba.bicicleta.publica.badajoz.objects.EstacionList;
import biba.bicicleta.publica.badajoz.objects.Message;
import biba.bicicleta.publica.badajoz.objects.MessageList;
import biba.bicicleta.publica.badajoz.utils.CommentsRequest;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;
import biba.bicicleta.publica.badajoz.utils.StationsRequest;

public class EstacionDetallesActivity extends AppCompatActivity {

    RecyclerView messagesRecycler;
    private GeneralSwipeRefreshLayout swipeLayout;
    private final SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        EstacionDetallesAdapter eda = new EstacionDetallesAdapter(createList(30));
        messagesRecycler.setAdapter(eda);
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(this);
        performRequest(false);
    }

    private void initSwipeLayout() {
        swipeLayout = (GeneralSwipeRefreshLayout) findViewById(
                R.id.activity_main_swipe_refresh_layout);

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

                        EstacionDetallesAdapter eda = new EstacionDetallesAdapter(createList(30));
                        messagesRecycler.setAdapter(eda);
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

        CommentsRequest request = new CommentsRequest();
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
        spiceManager.execute(request, "cache", DurationInMillis.ONE_MINUTE, new MessageListRequestListener());
    }

    public List<Message> createList(int number) {
        ArrayList<Message> msgList = new ArrayList<Message>();
        Date dat;
        dat = Date.valueOf("2007-09-23 10:10:10.0");

        for (int i = 0; i < number; i++) {
            msgList.add(new Message("something", dat));
        }

        return msgList;
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
        public void onRequestSuccess(MessageList estaciones) {

        }
    }
}

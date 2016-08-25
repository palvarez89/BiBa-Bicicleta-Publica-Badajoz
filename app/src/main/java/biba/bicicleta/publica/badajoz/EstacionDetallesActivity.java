package biba.bicicleta.publica.badajoz;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import biba.bicicleta.publica.badajoz.adapters.EstacionDetallesAdapter;
import biba.bicicleta.publica.badajoz.objects.Message;

public class EstacionDetallesActivity extends AppCompatActivity {

    RecyclerView messagesRecycler;

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

    public List<Message> createList(int number) {
        ArrayList<Message> msgList = new ArrayList<Message>();
        Timestamp ts;
        ts = Timestamp.valueOf("2007-09-23 10:10:10.0");

        for (int i = 0; i < number; i++) {
            msgList.add(new Message("something", ts));
        }

        return msgList;
    }
}

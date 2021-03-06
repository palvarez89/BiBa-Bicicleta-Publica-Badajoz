package biba.bicicleta.publica.badajoz;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.apache.commons.lang3.StringEscapeUtils;

import biba.bicicleta.publica.badajoz.adapters.EstacionDetallesAdapter;
import biba.bicicleta.publica.badajoz.objects.Estacion;
import biba.bicicleta.publica.badajoz.objects.MessageList;
import biba.bicicleta.publica.badajoz.utils.Analytics;
import biba.bicicleta.publica.badajoz.utils.CommentPut;
import biba.bicicleta.publica.badajoz.utils.CommentsRequest;
import biba.bicicleta.publica.badajoz.utils.Common;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;
import biba.bicicleta.publica.badajoz.views.EstacionViewHolder;

public class EstacionDetallesActivity extends AppCompatActivity {

    final Context context = this;
    RecyclerView messagesRecycler;
    FloatingActionButton fab;
    private GeneralSwipeRefreshLayout swipeLayout;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);
    private int stationNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Estacion e = getIntent().getParcelableExtra("estacion");
        stationNumber = e.getN();

        setContentView(R.layout.activity_estacion_detalles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Analytics analytics = new Analytics(this);
        analytics.screenView("EstacionDetallesActivity.java");

        initSwipeLayout();

        initFab();
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
        performRequest();
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
        CardView topCardView = (CardView) findViewById(R.id.top_card_detalles);

        topCardView.setRadius(0);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topCardView.getLayoutParams();
        params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
        topCardView.setLayoutParams(params);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
        topCardView.setCardElevation(px);

        final EstacionViewHolder evh = EstacionViewHolder.newInstance(topCardView);
        int numero = e.getN();
        String nombre = Common.toLowerCase(e.getName());
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

        evh.mFavStar.setOnClickListener(new FavClickListener(isFav) {
            @Override
            public void onClick(View v) {
                isFav = !isFav;
                evh.setFavStar(isFav);
                prefs.edit().putBoolean("fav" + realPosition, isFav).apply();

            }
        });
    }

    private void initFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get prompts.xml view
                LinearLayout mLinearLayout = new LinearLayout(getApplicationContext());
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.new_comment, mLinearLayout);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);


                // set dialog message
                alertDialogBuilder
                        .setTitle(R.string.comment_here)
                        .setCancelable(false)
                        .setPositiveButton(R.string.add,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String newComment = StringEscapeUtils.escapeJava(userInput.getText().toString());
                                        CommentPut request = new CommentPut(stationNumber, newComment);
                                        spiceManager.execute(request, "add-comment-cache" + stationNumber, DurationInMillis.ONE_SECOND, new MessagePutRequestListener());
                                        Analytics analytics = new Analytics((Activity) context);
                                        analytics.screenView("Comment add");
                                    }
                                })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();

                // disable positive button sometimes

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {
                        ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                });

                userInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int length = s.length();
                        // Check if edittext is empty
                        if (length <= 0) {
                            // Disable ok button
                            alertDialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else if (length > 140){
                            // edit text larger than maximum
                            alertDialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            // Something into edit text. Enable the button.
                            alertDialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }
                    }
                });
                // show it
                alertDialog.show();

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
                        performRequest();
                    }
                });
            }
        });
    }

    public void performRequest() {
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
            Toast.makeText(getApplicationContext(), R.string.failed_fetch_comments,
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
            EstacionDetallesAdapter eda = new EstacionDetallesAdapter(messageList, spiceManager, (Activity) context);
            messagesRecycler.setAdapter(eda);
        }
    }

    public abstract class FavClickListener implements View.OnClickListener {
        boolean isFav;

        public FavClickListener(boolean isFav) {
            this.isFav = isFav;
        }
    }

    private class MessagePutRequestListener implements RequestListener<String> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getApplicationContext(), R.string.failed_add_comment,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(String result) {
            performRequest();
        }
    }
}

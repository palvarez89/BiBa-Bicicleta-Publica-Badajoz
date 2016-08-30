package biba.bicicleta.publica.badajoz.adapters;


import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;

import java.util.List;

import biba.bicicleta.publica.badajoz.EstacionDetallesActivity;
import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.objects.Message;
import biba.bicicleta.publica.badajoz.utils.Analytics;
import biba.bicicleta.publica.badajoz.utils.CommentArchive;
import biba.bicicleta.publica.badajoz.views.EstacionDetallesViewHolder;

public class EstacionDetallesAdapter extends RecyclerView.Adapter<EstacionDetallesViewHolder> {

    private List<Message> messageList;
    private SpiceManager spiceManager;
    private Activity edActivity;

    public EstacionDetallesAdapter(List<Message> messageList, SpiceManager spiceManager, Activity edAct) {
        this.messageList = messageList;
        this.spiceManager = spiceManager;
        this.edActivity = edAct;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onBindViewHolder(EstacionDetallesViewHolder estacionDetallesViewHolder, int i) {
        final Message msg = messageList.get(i);

        estacionDetallesViewHolder.setDetails(msg);
        estacionDetallesViewHolder.vArchive.setOnClickListener(new ArchiveClickListener(msg.getId()) {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(edActivity, R.style.AlertDialogCustom));

                // set dialog message
                alertDialogBuilder
                        .setTitle(R.string.delete_comment)
                        .setMessage(R.string.delete_dialog_msg)
                        .setCancelable(false)
                        .setPositiveButton(R.string.delete,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        CommentArchive request = new CommentArchive(msg.getId());
                                        spiceManager.execute(request, "archive-comment-cache" + msg.getId(), DurationInMillis.ONE_SECOND, new MessageArchiveRequestListener());
                                        Analytics analytics = new Analytics(edActivity);
                                        analytics.screenView("Comment deleted");
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

                // show it
                alertDialog.show();
            }
        });
    }

    @Override
    public EstacionDetallesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.estacion_details_card, viewGroup, false);

        return new EstacionDetallesViewHolder(itemView);
    }

    public abstract class ArchiveClickListener implements View.OnClickListener {
        int commentId;

        public ArchiveClickListener(int id) {
            this.commentId = id;
        }
    }


    public class MessageArchiveRequestListener implements com.octo.android.robospice.request.listener.RequestListener<String> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(edActivity, R.string.failed_delete_comment,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(String s) {
            ((EstacionDetallesActivity) edActivity).performRequest();
        }
    }
}

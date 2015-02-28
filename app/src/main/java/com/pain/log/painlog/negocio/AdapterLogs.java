package com.pain.log.painlog.negocio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pain.log.painlog.R;

import java.util.ArrayList;

/**
 * Created by RULO on 01/12/2014.
 */
public class AdapterLogs extends RecyclerView.Adapter<AdapterLogs.ViewHolder> {


    public static final int LAST_POSITION = -1;


    private ArrayList<Logs> items = new ArrayList<>();
    private Activity activity;


    public AdapterLogs(Activity activity, ArrayList<Logs> items) {

        this.items = items;
        this.activity = activity;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {




        public ViewHolder(View container) {
            super(container);

          //  layo = (LinearLayout) container.findViewById(R.id.layo);


        }
    }



    public ArrayList<Logs> getItems() {
        return items;
    }

    public void setItems(ArrayList<Logs> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public AdapterLogs.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dummy_l, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);


        return holder;


    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {



    }

    public void add(Logs s, int position) {

        position = position == LAST_POSITION ? getItemCount() : position;
        items.add(position, s);
        notifyItemInserted(position);
    }

}




package com.pain.log.painlog.negocio;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pain.log.painlog.R;

import java.util.ArrayList;

/**
 * Created by RULO on 01/12/2014.
 */
public class AdapterProyectos extends RecyclerView.Adapter<AdapterProyectos.ViewHolder> {


    public static final int LAST_POSITION = -1;


    private ArrayList<Diarios> items = new ArrayList<>();
    private Activity activity;


    public AdapterProyectos(Activity activity, ArrayList<Diarios> items) {

        this.items = items;
        this.activity = activity;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Text;

        public ViewHolder(View container) {
            super(container);

            Text = (TextView) container.findViewById(R.id.text);



        }
    }



    public ArrayList<Diarios> getItems() {
        return items;
    }

    public void setItems(ArrayList<Diarios> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public AdapterProyectos.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dummy_p, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;


    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.Text.setText(items.get(i).getNombre());
        viewHolder.Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,Integer.toString(items.get(i).getClave()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void add(Diarios s, int position) {

        position = position == LAST_POSITION ? getItemCount() : position;
        items.add(position, s);
        notifyItemInserted(position);
    }

}




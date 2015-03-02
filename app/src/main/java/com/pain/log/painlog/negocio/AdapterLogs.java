package com.pain.log.painlog.negocio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pain.log.painlog.R;

import java.util.ArrayList;

import static com.pain.log.painlog.negocio.LogUtils.LOGI;

/**
 * Created by RULO on 01/12/2014.
 */
public class AdapterLogs extends RecyclerView.Adapter<AdapterLogs.ViewHolder> {


    public static final int LAST_POSITION = -1;


    private ArrayList<Logs> items = new ArrayList<>();
    private Activity activity;
    private MoonCalculation luna = new MoonCalculation();


    public AdapterLogs(Activity activity, ArrayList<Logs> items) {

        this.items = items;
        this.activity = activity;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textNotas;
        TextView textLuna;
        TextView textFecha;
        ImageView imageLuna;
        LinearLayout viewLay;
        TextView textDolor;
        ImageButton delete;
        ImageButton edit;


        public ViewHolder(View container) {
            super(container);

            viewLay = (LinearLayout) container.findViewById(R.id.viewLay);
            textLuna = (TextView) container.findViewById(R.id.textLuna);
            textNotas = (TextView) container.findViewById(R.id.textNotas);
            textFecha = (TextView) container.findViewById(R.id.textFecha);
            textDolor = (TextView) container.findViewById(R.id.textDolor);
            imageLuna = (ImageView) container.findViewById(R.id.imageLuna);
            delete = (ImageButton) container.findViewById(R.id.delete);
            edit = (ImageButton) container.findViewById(R.id.edit);

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

        viewHolder.textFecha.setText(items.get(i).getFecha());
        if (items.get(i).getNotas().toString().trim().length() == 0)
            viewHolder.textNotas.setText(activity.getResources().getString(R.string.sinnotas));
        else
            viewHolder.textNotas.setText(items.get(i).getNotas());

        int fase =luna.moonPhase(items.get(i).getFecha());
        viewHolder.textLuna.setText(luna.phaseName(activity,fase));
        viewHolder.imageLuna.setImageDrawable(luna.phaseImage(activity,fase));

        setDolor(viewHolder, items.get(i).getIntensidad());


        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(R.string.confirmar)
                        .setCancelable(true)
                        .setTitle(R.string.eliminarTittle)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int clave = items.get(i).getClave();
                                int clave_d = items.get(i).getClave_d();
                                items.remove(i);
                                notifyDataSetChanged();
                                ((LogActivity) activity).deleteItem(clave,clave_d);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int clave = items.get(i).getClave();
                int clave_d = items.get(i).getClave_d();

                Intent intent = new Intent(activity, DolActivity.class);
                intent.putExtra("CLAVE",clave_d);
                intent.putExtra("CLAVE_I",clave);
                LOGI("VALOR CLAVE", Integer.toString(clave_d));
                LOGI("VALOR CLAVE_I", Integer.toString(clave));
                intent.putExtra("SERVICIO", "UPD");
                activity.startActivity(intent);
            }
        });


    }

    private void setDolor(ViewHolder viewHolder, int progress){

        if (progress <= 32) {
            viewHolder.viewLay.setBackgroundColor(activity.getResources().getColor(R.color.color_b));
            viewHolder.textDolor.setText(R.string.dolor1);
        } else if (progress >= 33 && progress <= 65) {
            viewHolder.viewLay.setBackgroundColor(activity.getResources().getColor(R.color.color_f));
            viewHolder.textDolor.setText(R.string.dolor2);
        } else if (progress >= 66) {
            viewHolder.viewLay.setBackgroundColor(activity.getResources().getColor(R.color.color_j));
            viewHolder.textDolor.setText(R.string.dolor3);
        }
    }

    public void add(Logs s, int position) {

        position = position == LAST_POSITION ? getItemCount() : position;
        items.add(position, s);
        notifyItemInserted(position);
    }

}




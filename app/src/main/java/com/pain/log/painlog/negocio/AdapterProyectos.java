package com.pain.log.painlog.negocio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
        ImageButton delete;
        ImageButton edit;
        ImageButton export;


        public ViewHolder(View container) {
            super(container);

            Text = (TextView) container.findViewById(R.id.text);
            delete = (ImageButton) container.findViewById(R.id.delete);
            edit = (ImageButton) container.findViewById(R.id.edit);
            export = (ImageButton) container.findViewById(R.id.export);

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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,Integer.toString(items.get(i).getClave()),Toast.LENGTH_SHORT).show();
            }
        });

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
                                items.remove(i);
                                notifyDataSetChanged();
                                ((DiariosActivity)activity).deleteItem(clave);
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
                final View view = activity.getLayoutInflater().inflate(R.layout.edittext, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setCancelable(true)
                        .setTitle(R.string.editarTittle)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                EditText editText = (EditText) view.findViewById(R.id.edittext);
                                int clave = items.get(i).getClave();
                                String titu = editText.getText().toString();

                                if (editText.getText().toString().trim().length()== 0){
                                    Toast.makeText(activity,R.string.errorvacio,Toast.LENGTH_SHORT).show();
                                } else {

                                items.get(i).setNombre(titu);
                                notifyDataSetChanged();
                                ((DiariosActivity)activity).editItem(clave, titu);
                            }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.setView(view);
                builder.show();
            }
        });

    }







    public void add(Diarios s, int position) {

        position = position == LAST_POSITION ? getItemCount() : position;
        items.add(position, s);
        notifyItemInserted(position);
    }

}




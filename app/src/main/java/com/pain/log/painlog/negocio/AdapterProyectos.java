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

import static com.pain.log.painlog.negocio.LogUtils.LOGI;

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

        TextView text;
        ImageButton delete;
        ImageButton edit;
        ImageButton export;
        LinearLayout layo;


        public ViewHolder(View container) {
            super(container);

            layo = (LinearLayout) container.findViewById(R.id.layo);
            text = (TextView) container.findViewById(R.id.text);
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

        viewHolder.text.setText(items.get(i).getNombre());
        viewHolder.layo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,LogActivity.class);
                intent.putExtra("CLAVE",items.get(i).getClave());
                intent.putExtra("NOMBRE",items.get(i).getNombre());
                LOGI("VALOR CLAVE", Integer.toString(items.get(i).getClave()));
                activity.startActivity(intent);
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
                                /*items.remove(i);
                                notifyDataSetChanged();*/
                                ((DiariosActivity) activity).deleteItem(clave);

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

                                if (editText.getText().toString().trim().length() == 0) {
                                    Toast.makeText(activity, R.string.errorvacio, Toast.LENGTH_SHORT).show();
                                } else {

                                   /*items.get(i).setNombre(titu);
                                    notifyDataSetChanged();*/
                                    ((DiariosActivity) activity).editItem(clave, titu);
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

        viewHolder.export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clave = items.get(i).getClave();
                ((DiariosActivity) activity).exportItem(clave, items.get(i).getNombre());
            }
        });


        viewHolder.delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(activity, activity.getResources().getString(R.string.eliminarTittle), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        viewHolder.edit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(activity, activity.getResources().getString(R.string.editarTittle), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        viewHolder.export.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(activity, activity.getResources().getString(R.string.exportTittle), Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }

}




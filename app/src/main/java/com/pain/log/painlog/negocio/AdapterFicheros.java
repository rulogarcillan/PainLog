package com.pain.log.painlog.negocio;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pain.log.painlog.R;

import java.util.ArrayList;

public class AdapterFicheros extends RecyclerView.Adapter<AdapterFicheros.ViewHolder> {

    private ArrayList<FicherosExcel> items = new ArrayList<>();
    private Activity activity;
    private int viewType;
    OnItemClickListener mItemClickListener;

    public AdapterFicheros(Activity activity, ArrayList<FicherosExcel> items, int viewType) {

        this.items = items;
        this.activity = activity;
        this.viewType = viewType;

    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public ArrayList<FicherosExcel> getItems() {
        return items;
    }

    public void setItems(ArrayList<FicherosExcel> items) {
        this.items = items;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView iconFile;
        TextView textName, textDate, textTam;



        public ViewHolder(View container) {
            super(container);

            if (viewType == FicherosExcel.TYPE_LARGE) {

                iconFile = (ImageView) container.findViewById(R.id.iconFile);
                textName = (TextView) container.findViewById(R.id.textName);
                textDate = (TextView) container.findViewById(R.id.textDate);
                textTam = (TextView) container.findViewById(R.id.textTam);

                container.setOnClickListener(this);

            } else if (viewType == FicherosExcel.TYPE_SHORT) {


            }


        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public AdapterFicheros.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == FicherosExcel.TYPE_LARGE) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_l, parent, false);
            ViewHolder vhItem = new ViewHolder(v);

            return vhItem;

        } else if (viewType == FicherosExcel.TYPE_SHORT) {




        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {



        if (viewType == FicherosExcel.TYPE_LARGE) {


            viewHolder.textName.setText(items.get(i).getNombre());
            viewHolder.textDate.setText(items.get(i).getFecha());
            viewHolder.textTam.setText(items.get(i).getTamaño());


        } else if (viewType == FicherosExcel.TYPE_SHORT) {




        }


    }



}
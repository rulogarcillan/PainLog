package com.pain.log.painlog.negocio;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pain.log.painlog.R;

import java.util.ArrayList;

public class AdapterDrawer extends RecyclerView.Adapter<AdapterDrawer.ViewHolder> {


    private ArrayList<MenuDrawer> items = new ArrayList<>();
    private Activity activity;
    OnItemClickListener mItemClickListener;


    public AdapterDrawer(Activity activity, ArrayList<MenuDrawer> items) {

        this.items = items;
        this.activity = activity;

    }


    public ArrayList<MenuDrawer> getItems() {
        return items;
    }

    public void setItems(ArrayList<MenuDrawer> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        ImageView rowIcon;
        TextView rowText;
        TextView textFather;
        LinearLayout back;

        public ViewHolder(View container, int ViewType) {
            super(container);

            if (ViewType == MenuDrawer.TYPE_HEADER) {

                textFather = (TextView) container.findViewById(R.id.textFather);

            } else if (ViewType == MenuDrawer.TYPE_ITEM) {

                rowIcon = (ImageView) container.findViewById(R.id.rowIcon);
                rowText = (TextView) container.findViewById(R.id.rowText);
                container.setOnClickListener(this);

            } else if (ViewType == MenuDrawer.TYPE_GRAN_HEADER) {

                back = (LinearLayout) container.findViewById(R.id.back);
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
    public AdapterDrawer.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {

        if (viewType == MenuDrawer.TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_father, parent, false);

            ViewHolder vhItem = new ViewHolder(v, viewType);

            return vhItem;

        } else if (viewType == MenuDrawer.TYPE_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_child, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType);

            return vhHeader;


        } else if (viewType == MenuDrawer.TYPE_GRAN_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_grandfather, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType);

            return vhHeader;


        }


        return null;

    }


    @Override
    public void onBindViewHolder(AdapterDrawer.ViewHolder viewHolder, final int i) {

        if (items.get(i).getType() == MenuDrawer.TYPE_HEADER) {

            viewHolder.textFather.setText(items.get(i).getTitulo());

        } else if (items.get(i).getType() == MenuDrawer.TYPE_ITEM) {

            viewHolder.rowText.setText(items.get(i).getTitulo());
            viewHolder.rowIcon.setImageResource(items.get(i).getIcono());


        }   else if (items.get(i).getType() == MenuDrawer.TYPE_ITEM) {


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType() == MenuDrawer.TYPE_HEADER) {
            return MenuDrawer.TYPE_HEADER;


        } else if (items.get(position).getType() == MenuDrawer.TYPE_ITEM) {

            return MenuDrawer.TYPE_ITEM;

        } else if (items.get(position).getType() == MenuDrawer.TYPE_GRAN_HEADER) {

            return MenuDrawer.TYPE_GRAN_HEADER;
        }
        return -1;

    }




}
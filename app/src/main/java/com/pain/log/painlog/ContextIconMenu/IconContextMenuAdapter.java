package com.pain.log.painlog.ContextIconMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pain.log.painlog.R;

public class IconContextMenuAdapter extends BaseAdapter {
    private Context context;
    private Menu menu;
    
    public IconContextMenuAdapter(Context context, Menu menu) {
		this.context = context;
		this.menu = menu;
	}

	@Override
	public int getCount() {
		return menu.size();
	}
	
	@Override
	public MenuItem getItem(int position) {
		return menu.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getItemId();
	}
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuItem item = getItem(position);
        
        TextView res = (TextView) convertView;
        if (res == null) {
        	res = (TextView) LayoutInflater.from(context).inflate(R.layout.contextmenuicon, null);
        }

        res.setTag(item);
        res.setText(item.getTitle());
        res.setCompoundDrawablesWithIntrinsicBounds(item.getIcon(), null, null, null);
              
        return res;
    }
}
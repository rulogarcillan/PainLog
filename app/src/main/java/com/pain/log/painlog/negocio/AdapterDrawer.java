package com.pain.log.painlog.negocio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.Libs;
import com.pain.log.painlog.R;

import java.util.ArrayList;

import de.cketti.library.changelog.ChangeLog;

public class AdapterDrawer extends RecyclerView.Adapter<AdapterDrawer.ViewHolder> {


    private ArrayList<MenuDrawer> items = new ArrayList<>();
    private Activity activity;


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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView rowIcon;
        TextView rowText;
        TextView textFather;
        LinearLayout rowLayout, back;



        public ViewHolder(View container, int ViewType) {
            super(container);

            if (ViewType == MenuDrawer.TYPE_HEADER) {

                textFather = (TextView) container.findViewById(R.id.textFather);

            } else if (ViewType == MenuDrawer.TYPE_ITEM) {

                rowIcon = (ImageView) container.findViewById(R.id.rowIcon);
                rowText = (TextView) container.findViewById(R.id.rowText);
                rowLayout = (LinearLayout) container.findViewById(R.id.rowLayout);

            } else if (ViewType == MenuDrawer.TYPE_GRAN_HEADER) {

                back = (LinearLayout) container.findViewById(R.id.back);
            }


        }
    }


    @Override
    public AdapterDrawer.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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


            viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (items.get(i).getTitulo() == R.string.puntuar) {
                        LanzaRate();
                        ((MainActivity) activity).mDrawerLayout.closeDrawers();
                    } else if (items.get(i).getTitulo() == R.string.license) {
                        lanzaLicense();
                        ((MainActivity) activity).mDrawerLayout.closeDrawers();
                    } else if (items.get(i).getTitulo() == R.string.more) {
                        LanzaMore();
                        ((MainActivity) activity).mDrawerLayout.closeDrawers();
                    } else if (items.get(i).getTitulo() == R.string.changelog) {
                        new LanzaChangelog(activity).getFullLogDialog().show();
                        ((MainActivity) activity).mDrawerLayout.closeDrawers();
                    } else if (items.get(i).getTitulo() == R.string.explorar){
                        lanzaExplorer();
                        ((MainActivity) activity).mDrawerLayout.closeDrawers();
                    }  else if (items.get(i).getTitulo() == R.string.exportall){
                        ((MainActivity) activity).fragment.exportAllItem();
                        ((MainActivity) activity).mDrawerLayout.closeDrawers();
                    }




                }
            });
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


    /**
     * *****************************************************
     */


    private void LanzaRate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.pain.log.painlog"));
        activity.startActivity(intent);
    }


    private void LanzaMore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Raúl R."));
        activity.startActivity(intent);
    }


    public static class LanzaChangelog extends ChangeLog {

        public static final String DEFAULT_CSS =

                "body {                                                           " + "	font-family: Verdana, Helvetica, Arial, sans-serif;   " + "	font-size: 11px;                                      " + "	color: #000000;                                       " + "	background-color: #ffffff;                            " + "	margin: 0px;                                          " + "	padding: 0px;                                         " + "}                                                        "
                        + "h1 {                                                     " + "	font-size: 14px;                                      " + "	font-weight: bold;                                    " + "	text-transform: uppercase;                            " + "	color: #000000;                                       " + "	margin: 0px;                                          " + "	padding: 10px 0px 0px 8px;                            " + "}                                                        "
                        + "h2 {                                                     " + "	font-size: 10px;                                      " + "	color: #999999;                                       " + "	font-weight: normal;                                  " + "	margin: 0px 0px 0px 8px;                              " + "	padding: 0px;                                         " + "}                                                        " + "ul {                                                     "
                        + "	margin: 0px 0px 10px 15px;                            " + "	padding-left: 15px;                                " + "	padding-top: 8px;                                     " + "	list-style-type: square;                              " + "	color: #999999;                                       " + "}";

        public LanzaChangelog(Context context) {
            super(new ContextThemeWrapper(context, R.style.AppTheme), DEFAULT_CSS);
        }
    }


    public void lanzaLicense() {

        new Libs.Builder()
                //Pass the fields of your application to the lib so it can find all external lib information
                .withFields(R.string.class.getFields())
                .withVersionShown(true)
                .withLicenseShown(true)
                .withAutoDetect(true)
                .withLibraries("sqliteassethelper")
                .withActivityTitle(activity.getResources().getString(R.string.license))
                .withAboutDescription(activity.getResources().getString(R.string.escrita) + "<br/><br/><b>License GNU GPL V3.0</b><br/><br/><a href=\"https://github.com/rulogarcillan/PainLog\">Project in Github</a>")
                .withActivityTheme(R.style.AppTheme)
                        //start the activity
                .start(activity);
    }

    public void  lanzaExplorer(){


        Intent intent = new Intent(activity,ExplorerActivity.class);
        activity.startActivity(intent);
    }


}
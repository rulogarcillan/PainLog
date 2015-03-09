package com.pain.log.painlog.Constantes;

import com.pain.log.painlog.R;

import com.pain.log.painlog.negocio.MenuDrawer;

import java.util.ArrayList;

/**
 * Created by Rulo on 08/03/2015.
 */
public class Constantes {

    public Constantes() {
    }

    private static final int titulo[] = {R.string.miscellaneous,
                                         R.string.puntuar,
                                         R.string.changelog,
                                         R.string.license,
                                         R.string.more};

    private static final int icono[] = {-1,
                                        R.drawable.ic_action_rating,
                                        R.drawable.ic_action_changelog,
                                        R.drawable.ic_license,
                                        R.drawable.ic_more};

    private static final int type[] = {MenuDrawer.TYPE_HEADER,
                                       MenuDrawer.TYPE_ITEM,
                                       MenuDrawer.TYPE_ITEM,
                                       MenuDrawer.TYPE_ITEM,
                                       MenuDrawer.TYPE_ITEM};


    public static ArrayList<MenuDrawer> genItemsDrawerMenu() {

        ArrayList<MenuDrawer> items = new ArrayList<>();

        for (int x = 0; x < titulo.length; x++) {
            items.add(new MenuDrawer(type[x], titulo[x], icono[x]));
        }
        return items;


    }



}
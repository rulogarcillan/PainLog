package com.pain.log.painlog.BD;

import android.content.Context;
import android.database.Cursor;

import com.pain.log.painlog.negocio.Diarios;

import java.util.ArrayList;

import static com.pain.log.painlog.negocio.LogUtils.LOGI;


public class Consultas {

    MyDatabase db;
    Context c;
    public static final String LEER = "R";
    public static final String ESCRIBIR = "W";

    public Consultas() {

    }

    public Consultas(Context c) {

        this.c = c;
        db = new MyDatabase(c);

    }


    public ArrayList getDiarios() {

        String sql;
        sql = "select * from diarios";
        LOGI("getDiarios", sql);
        ArrayList<Diarios> array = new ArrayList<>();
        Cursor cur = db.query(sql, LEER);
        if (cur.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {

                Diarios item = new Diarios(cur.getInt(0), cur.getString(1));
                array.add(item);

            } while (cur.moveToNext());
        }

        db.close();
        return array;
    }

}

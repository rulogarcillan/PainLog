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

    public Integer genKeyIdTabla(String tabla) {


        Cursor cur = null;
        String query;

        query = "select ifnull(max(clave),0) + 1 from " + tabla;
        cur = db.query(query, LEER);
        LOGI("genKeyIdTabla", query);

        if (cur.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {

                return (cur.getInt(0));

            } while (cur.moveToNext());
        }
        db.close();
        return null;

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

    public String addDiario(Diarios datos) {

        String sql;
        sql = "Insert into diarios (nombre) values ('" + datos.getNombre() + "')";
        LOGI("addDiario", sql);

        Cursor cur = db.query(sql, ESCRIBIR);
        if (cur.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {
                return (cur.getString(0));

            } while (cur.moveToNext());
        }
        db.close();
        return "0";

    }


       public void deleteDiario(int clave) {


        Cursor cur = null;
        String query, query2;

        query = "delete from diarios where clave = " + clave;
        query2 = "delete from registros where diarios_clave = " + clave;
        LOGI("deleteDiario", query2);
        cur = db.query(query2, ESCRIBIR); //Borra
        if (cur.moveToFirst()) {
            do {

            } while (cur.moveToNext());
        }

        LOGI("deleteDiario", query);
        cur = db.query(query, ESCRIBIR); //Borra proyectos
        if (cur.moveToFirst()) {
            do {

            } while (cur.moveToNext());
        }

        db.close();

    }

    public void editDiario(int clave, String titu) {


        Cursor cur = null;
        String query;

        query = "update diarios set nombre='" + titu + "' where clave = " + clave;

        LOGI("editDiario", query);
        cur = db.query(query, ESCRIBIR); //Borra
        if (cur.moveToFirst()) {
            do {

            } while (cur.moveToNext());
        }


        db.close();

    }


}

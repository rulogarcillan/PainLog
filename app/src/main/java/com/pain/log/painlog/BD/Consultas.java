package com.pain.log.painlog.BD;

import android.content.Context;
import android.database.Cursor;

import com.pain.log.painlog.negocio.Diarios;
import com.pain.log.painlog.negocio.Logs;
import com.pain.log.painlog.negocio.Ordena;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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

    public Integer genKeyIdTablaDia() {
        Cursor cur = null;
        String query;

        query = "select ifnull(max(clave),0) + 1 from diarios";
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

    public Integer genKeyIdTablaReg() {


        Cursor cur = null;
        String query;

        query = "select ifnull(max(clave_r),0) + 1 from registros";
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


    public String addRegistros(Logs datos) {

        String sql;
        sql = "Insert into registros (fecha, intensidad, notas, diarios_clave) values ('" + datos.getFecha() + "'," + datos.getIntensidad() + ",'" + datos.getNotas() + "'," + datos.getClave_d() + ")";
        LOGI("addRegistros", sql);

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


    public ArrayList getLogs(int clave) {

        String sql;
        sql = "select * from registros where diarios_clave = " + clave + " order by fecha desc";
        LOGI("getLogs", sql);
        ArrayList<Logs> array = new ArrayList<>();
        Cursor cur = db.query(sql, LEER);
        if (cur.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {

                Logs item = new Logs(cur.getInt(0),cur.getString(1), cur.getInt(2), cur.getString(3), cur.getInt(4));
                array.add(item);

            } while (cur.moveToNext());
        }

        db.close();
        Collections.sort(array, new Ordena());
        return array;
    }


    public Logs getOneLog(int clave, int clave_d) {

        String sql;
        sql = "select * from registros where diarios_clave = " + clave_d + " and clave_r = " + clave;
        LOGI("getLogs", sql);
        Logs item = new Logs();
        Cursor cur = db.query(sql, LEER);
        if (cur.moveToFirst()) {
            // Recorremos el cursor hasta que no haya más registros
            do {

                item = new Logs(cur.getInt(0), cur.getString(1), cur.getInt(2), cur.getString(3), cur.getInt(4));


            } while (cur.moveToNext());
        }

        db.close();

       return item;
    }

    public void deleteLog(int clave, int clave_d) {

        Cursor cur = null;
        String query;

        query = "delete from registros where diarios_clave = " + clave_d + " and clave_r = " + clave;
        LOGI("deleteLog", query);
        cur = db.query(query, ESCRIBIR); //Borra
        if (cur.moveToFirst()) {
            do {

            } while (cur.moveToNext());
        }

        db.close();

    }


    public void editlog(Logs item) {

        Cursor cur = null;
        String query;

        query = "update registros set notas='" + item.getNotas() + "', fecha = '" + item.getFecha() + "', intensidad = " + item.getIntensidad() + " where diarios_clave = " + item.getClave_d() + " and clave_r = " + item.getClave();

        LOGI("editlog", query);
        cur = db.query(query, ESCRIBIR); //Borra
        if (cur.moveToFirst()) {
            do {

            } while (cur.moveToNext());
        }


        db.close();

    }



}

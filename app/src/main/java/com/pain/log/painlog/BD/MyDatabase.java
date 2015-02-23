package com.pain.log.painlog.BD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class MyDatabase extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "DBsqlite.db";
    private static final int DATABASE_VERSION = 1;
    public static final String LEER = "R";
    public static final String ESCRIBIR = "W";

    public MyDatabase(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public Cursor query(String sql, String modo) {

        SQLiteDatabase db = null;

        if (modo.equals(LEER)) {
            db = getReadableDatabase();
        } else if (modo.equals(ESCRIBIR)) {
            db = getWritableDatabase();
        }
        Cursor c = db.rawQuery(sql, null);

        return c;
    }


}
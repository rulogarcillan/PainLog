package com.pain.log.painlog.negocio;

import android.os.Environment;
import android.util.Log;

import com.pain.log.painlog.BD.MyDatabase;
import com.pain.log.painlog.BuildConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class LogUtils {

    static final String TAG = "DEBUG_SELFIE: ";

    public static void LOGD(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }

    public static void LOGV(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }

    public static void LOGI(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }

    public static void LOGW(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }

    public static void LOGE(final String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + tag, message);
        }
    }


    public static final void copybd() {

        if (BuildConfig.DEBUG) {
            try {
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/" + MyDatabase.DATABASE_NAME;
                    String backupDBPath = BuildConfig.APPLICATION_ID + ".db";
                    File currentDB = new File(currentDBPath);
                    File backupDB = new File(sd, backupDBPath);

                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    }
                }
            } catch (Exception e) {
                LOGI("copybd", "No copiada");
            }
        }
    }


}
package com.pain.log.painlog.export;

import android.os.Environment;

/**
 * Created by rulo on 19/03/15.
 */
public class BackUp {

    public static final String root = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PainLog/backup";
}

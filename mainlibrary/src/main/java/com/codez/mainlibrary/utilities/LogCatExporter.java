package com.codez.mainlibrary.utilities;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Eptron on 11/6/2015.
 */
public class LogCatExporter {

    private String mPackageName;
    private Context mContext;

    public LogCatExporter(Context context) {
        mPackageName = context.getPackageName();
        mContext = context;
    }

    public void export(String fileName) {
        File filename = new File(Environment.getExternalStorageDirectory() + "/" + fileName + ".log");
        try {
            if (filename.exists())
                filename.delete();
            filename.createNewFile();
            String cmd = "logcat -d -f" + filename.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
            Toast.makeText(mContext, "Log exported", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.codez.mainlibrary.utilities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Eptron on 12/15/2015.
 */
public class DiskUtils {

    public static boolean deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            Log.i("DiskUtils", "Atempting deletion of file:" + filename);
            return file.delete();
        }
        return false;
    }

    public static void writeObjectToDisk(Context context, Object obj, String filename) {
        Gson gson = new Gson();
        writeStringToDisk(context, gson.toJson(obj), filename);
    }

    public static void writeJsonToDisk(Context context, JSONObject object, String filename) {
        writeStringToDisk(context, object.toString(), filename);
    }

    public static void writeStringToDisk(Context context, String data, String filename) {
        File file = new File(filename);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.i("SaveRequest", "Write to internal file storage failed");
        }
    }

    @Nullable
    public static <T> T getObjectFromDisk(Context context, Class<T> clazz, String filename) {
        String fileStr = getStringFromDisk(context, filename);
        if (!fileStr.isEmpty()) {
            Gson gson = new Gson();
            return gson.fromJson(fileStr, clazz);
        }
        return null;
    }

    @Nullable
    public static <T> T getObjectFromDisk(Context context, Type type, String filename) {
        String fileStr = getStringFromDisk(context, filename);
        if (!TextUtils.isEmpty(fileStr)) {
            Gson gson = new Gson();
            return gson.fromJson(fileStr, type);
        }
        return null;
    }

    public static String getStringFromDisk(Context context, String filename) {
        String fileStr = "";
        try {
            FileInputStream fis = context.openFileInput(filename);
            StringBuilder builder = new StringBuilder();
            int ch;
            while ((ch = fis.read()) != -1) {
                builder.append((char) ch);
            }
            fileStr = builder.toString();
        } catch (IOException e) {
        }
        return fileStr;
    }
}

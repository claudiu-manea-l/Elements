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

    /**
     * Delets a file with that path if it exists
     * @param path
     * @return if it was successful or not
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            Log.i("DiskUtils", "Atempting deletion of file:" + path);
            return file.delete();
        }
        return false;
    }

    /**
     * Writes and object to a text file
     * @param context
     * @param obj The object to be writen
     * @param filename The name of the file to be writen to
     */
    public static void writeObjectToDisk(Context context, Object obj, String filename) {
        Gson gson = new Gson();
        writeStringToDisk(context, gson.toJson(obj), filename);
    }

    /**
     * Writes a JSONObject to a text file
     * @param context
     * @param jsonObject The jsonObject to be written to the given file
     * @param filename The name of the file to be written to
     */
    public static void writeJsonToDisk(Context context, JSONObject jsonObject, String filename) {
        writeStringToDisk(context, jsonObject.toString(), filename);
    }

    /**
     * Writes a string to the disk
     * @param context
     * @param data The string to be written to the given file
     * @param filename The name of the file to be written to
     */
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

    /**
     * Reads a String from the given file and returns an object of the given class
     * @param context
     * @param clazz the given class type of the object which will be returned
     * @param filename the name of the file
     * @param <T> the type of the object
     * @return the object retrieved from the file
     */
    @Nullable
    public static <T> T getObjectFromDisk(Context context, Class<T> clazz, String filename) {
        String fileStr = getStringFromDisk(context, filename);
        if (!fileStr.isEmpty()) {
            Gson gson = new Gson();
            return gson.fromJson(fileStr, clazz);
        }
        return null;
    }

    /**
     *  Reads a String from the given file and returns an object of the given class
     * @param context
     * @param type the given class type(can by a List<ObjectX> of the object which will be returned
     * @param filename the name of the file
     * @param <T> the type of the object
     * @return the object retrieved from the file
     */
    @Nullable
    public static <T> T getObjectFromDisk(Context context, Type type, String filename) {
        String fileStr = getStringFromDisk(context, filename);
        if (!TextUtils.isEmpty(fileStr)) {
            Gson gson = new Gson();
            return gson.fromJson(fileStr, type);
        }
        return null;
    }

    /**
     * Gets the context of the given filename as a String
     * @param context Context needed for openFileInput()
     * @param filename the filename to extract the String from
     * @return the contents of the file
     */
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

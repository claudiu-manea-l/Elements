package com.codez.mainlibrary.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Claudiu on 19/03/2015.
 * Utility class used for different purposes
 */
public class OtherUtils {

    /**
     * Checks if there is a Internet connection available (Simplest way, default to ConnectManager
     * for proper use)
     * @param context
     * @return
     */
    public static boolean hasInternet(Context context) {
        try {
            final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Replaces all spaces in the given text with commas(',')
     * @param text the text to be corrected
     * @return the text without spaces
     */
    public static String correctString(String text) {
        if (text.contains(" ")) text = text.replace(" ", ",");
        return text;
    }

    /**
     * Return the extension of the file (from path or url)
     * @param url the path/url/filename
     * @return the extension of the text
     */
    public static String getExtensionNormal(String url) {
        if(!TextUtils.isEmpty(url)) {
            int index = url.lastIndexOf(".");
            return url.substring(index + 1, url.length());
        }
        return "";
    }

    /**
     * Returns "Not Set" if a string is empty or the string if it's not
     * @param str the given string to be checked
     * @return
     */
    public static String checkSet(String str) {
        if (TextUtils.isEmpty(str)) return "Not set";
        else return str;
    }

    /**
     * Checks if the Integer supplied is null and if so returns 0
     * This normally happens when the backend supplies nulls for ints
     * @param number the number to be checked
     * @return
     */
    public static int checkIntNull(Integer number) {
        return (number == null) ? 0 : number;
    }

    /**
     * Check if a given string is empty or null and if so assigns it "N/A"
     * @param str the string to be checked
     * @return
     */
    public static String checkNull(String str) {
        if (str == null || TextUtils.isEmpty(str)) return "N/A";
        else return str;
    }

    /**
     * Checks if the given string is a url
     * @param url the string to be checked
     * @return
     */
    public static boolean isURL(String url) {
        if (!TextUtils.isEmpty(url)) {
            String http = url.substring(0, 3);
            if (http.equalsIgnoreCase("http"))
                return true;
        }
        return false;
    }

    /**
     * Encodes a file to Base64... Not so good, not to be used
     * @deprecated Use MultiPart for networking posts of files
     * @param file the file to be encoded
     * @return
     */
    @Deprecated
    public static String encodeFileToBase64Binary(File file) {
        String encoded = "";
        try {
            byte[] bytes = loadFile(file);
            encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoded;
    }

    /**
     * Encodes the contents of a File(given as a InputStream) to Base64... Not so good, not to be used
     * @deprecated Use MultiPart for networking posts of files
     * @param is the input stream of the file to be encoded
     * @return
     */
    public static String encodeFileToBase64Binary(InputStream is) {
        String encoded = "";
        try {
            byte[] bytes = loadFile(is);
            encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoded;
    }

    /**
     * Loads the bytes of a file from a InputStream
     * @param is
     * @return
     * @throws IOException
     */
    private static byte[] loadFile(InputStream is) throws IOException {
        long length = is.available();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + is.toString());
        }
        is.close();
        return bytes;
    }

    /**
     * Fetches the bytes of a given file
     * @param file
     * @return
     * @throws IOException
     */
    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        return loadFile(is);
    }

    /**
     * Converts a list if Strings into an array of String
     * (List<String> to String[])
     * @param list the list to be converted
     * @return Same list as an static array
     */
    public static String[] getArrayFromList(List<String> list) {
        String[] temp = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            temp[i] = list.get(i);
        return temp;
    }

    /**
     * Returns the name of the country indefied by the given country code
     * @param countryCode the country code of a country (Ex: "DE","UK"
     * @return the name of the country
     */
    public static String getCountry(String countryCode) {
        String defLocale = Locale.getDefault().getLanguage();
        Locale loc = new Locale(defLocale, countryCode);
        String temp = loc.getDisplayCountry();
        System.out.println("Code= " + countryCode + "+++ DefLocale = " + defLocale + "+++ Country name =" + temp);
        return loc.getDisplayCountry();
    }

    /**
     * Gets an alphabetical sorted List of all the Countries
     * @return
     */
    public static ArrayList<String> getCountries() {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        return countries;
    }

    /**
     * Changes the value of a String from "true" to "false"
     * (Can't remember why this stupid logic was needed... but it was for
     * some back-end related nonsense)
     * @param value
     * @return
     */
    public static String changeValue(String value) {
        boolean isChecked = getBoolean(value);
        String temp;
        if (isChecked) temp = "0";
        else temp = "1";
        return temp;
    }

    /**
     * Checks if the given String is 1 or 0
     * @param code
     * @return
     */
    public static boolean getBoolean(String code) {
        int isTrue = 0;
        if (code != null && !code.equals("null"))
            if (code.equals("1") || code.equals("0"))
                isTrue = Integer.valueOf(code);
        return isTrue > 0;
    }


    /**
     * Decodes a picture bitmap from a file by the set WIDTH and HEIGHT
     *
     * @param f      The file containing the picture
     * @param WIDTH  the requested width of the bitmap
     * @param HEIGHT the requested height of the bitmap
     * @return the bitmap resized to the desired width and height
     */
    public static Bitmap decodeFile(File f, int WIDTH, int HEIGHT) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HEIGHT = HEIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HEIGHT)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the Address of the Latitude & Longitude specified point
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    public static String getAddressLine(Context context, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        try {
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAddressLine(1);
            return address + " " + city;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the current moment in type under "MMdd_HHmmssSSS" format
     * @return
     */
    public static String getTimestamp() {
        return new SimpleDateFormat("MMdd_HHmmssSSS").format(new Date());
    }

    /**
     * Disable a View item completely
     * @param view
     */
    public static void disableItem(View view) {
        view.setEnabled(false);
        view.setClickable(false);
        view.setFocusable(false);
    }

    /**
     *
     * @param context
     * @param resId
     * @return
     */
    public static Drawable getColorDrawable(Context context, int resId) {
        int color = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(context, resId);
        } else {
            color = context.getResources().getColor(resId);
        }
        return new ColorDrawable(color);
    }

    /**
     *
     * @param resId
     * @param context
     * @return
     */
    public static Drawable getDrawable(int resId, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(resId, context.getTheme());
        } else {
            return context.getResources().getDrawable(resId);
        }
    }

    @SuppressLint("NewApi")
    private static int getColor(Context context, int resId) {
        return context.getResources().getColor(resId, context.getTheme());
    }

    /**
     * Sets the background for a View
     * Handles setBackground for both versions higher/lower than JellyBean
     *
     * @param v the View on which to set the background
     * @param d the background to set
     */
    public static void setBG(View v, Drawable d) {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB))
            setBackground(v, d);
        else setBackgroundDrawable(v, d);
    }

    @SuppressLint("NewApi")
    private static void setBackground(View v, Drawable d) {
        v.setBackground(d);
    }

    @SuppressWarnings("deprecation")
    private static void setBackgroundDrawable(View v, Drawable d) {
        v.setBackgroundDrawable(d);
    }

    /**
     * Writes some given text to the BitMap fetched from a resource id
     * @param gContext
     * @param gResId
     * @param gText
     * @return
     */
    public static Bitmap drawTextToBitmap(Context gContext,
                                          int gResId,
                                          String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =
                BitmapFactory.decodeResource(resources, gResId);

        Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(0, 0, 0));
        // text size in pixels
        paint.setTextSize((int) (14 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;
        canvas.drawText(gText, x, y, paint);
        return bitmap;
    }
}

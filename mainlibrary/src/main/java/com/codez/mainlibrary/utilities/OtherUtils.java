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
 * Created by eptron on 19/03/2015.
 * Utility class used for different purposes
 */
public class OtherUtils {

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

    public static String buildWaypointString(String[] waypoints, boolean isOptimized) {
        String waypointsString = "";
        if (isOptimized) {
            waypointsString = "optimize:true|";
        }
        String dashChar = "|";
        /*try {
            dashChar = URLEncoder.encode("|", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        int length = waypoints.length;
        if (waypoints.length > 8) {
            length = 8;
        }
        for (int i = 0; i < length; i++)
            if (i == 0) {
                waypointsString += waypoints[i];
            } else {
                waypointsString += dashChar + waypoints[i];
            }
        return waypointsString;
    }

    public static String correctString(String text) {
        if (text.contains(" ")) text = text.replace(" ", ",");
        return text;
    }

    public static String buildTime(int mHour, int mMinute, int mSeconds) {
        String hours = "", minutes = "", seconds = "";
        if (mHour < 10) hours = "0" + mHour;
        else hours = mHour + "";
        if (mMinute < 10) minutes = "0" + mMinute;
        else minutes = mMinute + "";
        if (mSeconds < 10) seconds = "0" + mSeconds;
        else seconds = mSeconds + "";
        String time = hours + ":" + minutes + ":" + seconds;
        return time;
    }

    public static String getExtensionNormal(String url) {
        int index = url.lastIndexOf(".");
        return url.substring(index + 1, url.length());
    }

    public static String getExtension(String url) {
        if (!TextUtils.isEmpty(url)) {
            int index = url.indexOf("?");
            if (index > 5) {
                String temp = url.substring(index - 4, index);
                if (temp.charAt(0) == '.')
                    temp = temp.substring(1, temp.length());
                return temp.toLowerCase();
            }
            return "";
        }
        return "";
    }

    public static boolean isSMSUrl(String url) {
        int pos = url.indexOf("//");
        try {
            String temp = url.substring(pos + 2, pos + 4);
            int num = Integer.parseInt(temp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String checkSet(String str) {
        if (TextUtils.isEmpty(str)) return "Not set";
        else return str;
    }

    public static int checkIntNull(Integer number) {
        return (number == null) ? 0 : number;
    }

    public static String checkNull(String str) {
        if (str == null || TextUtils.isEmpty(str)) return "N/A";
        else return str;
    }

    public static boolean isURL(String url) {
        if (!TextUtils.isEmpty(url)) {
            String http = url.substring(0, 3);
            if (http.equalsIgnoreCase("http"))
                return true;
        }
        return false;
    }

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

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
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
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public static String[] getArrayFromList(List<String> list) {
        String[] temp = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            temp[i] = list.get(i);
        return temp;
    }

    public static String getCountry(String countryCode) {
        String defLocale = Locale.getDefault().getLanguage();
        Locale loc = new Locale(defLocale, countryCode);
        String temp = loc.getDisplayCountry();
        System.out.println("Code= " + countryCode + "+++ DefLocale = " + defLocale + "+++ Country name =" + temp);
        return loc.getDisplayCountry();
    }

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

    public static String changeValue(String value) {
        boolean isChecked = getBoolean(value);
        String temp;
        if (isChecked) temp = "0";
        else temp = "1";
        return temp;
    }

    public static boolean getBoolean(String code) {
        int isTrue = 0;
        if (code != null && !code.equals("null"))
            if (code.equals("1") || code.equals("0"))
                isTrue = Integer.valueOf(code);
        return isTrue > 0;
    }

    /**
     * Decodes the polyline string received from Google Directions API
     * and adds the polyline points to an array
     *
     * @param encoded the encoded string
     * @return the array containing all the polylines in
     * coordinate(LatLng) format
     */
    public static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
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

    public static Uri getOutputMediaFileUri(int jobId, int type, int count) {
        String typeS = "";
        if (type == 1) typeS = "before";
        if (type == 2) typeS = "after";
        return Uri.fromFile(getOutputMediaFile(jobId, typeS, count));
    }

    public static File getOutputMediaFile(int jobId, String type, int count) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "smssolution");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i("PicturesFragment", "failed to create directory");
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File((mediaStorageDir.getPath() + File.separator
                //+ "eptron" + File.separator
                + getPictureName(jobId, type, count)));
        try {
            if (!mediaFile.exists())
                mediaFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaFile;
    }

    public static String getPictureName(int jobId, String type, int count) {
        return jobId + "_" + type + "_" + count + ".png";
    }

    public static File getOutputMediaFile(int jobId, String type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "smssolution");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i("PicturesFragment", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timestamp = getTimestamp();
        File mediaFile;
        mediaFile = new File((mediaStorageDir.getPath() + File.separator
                //+ "eptron" + File.separator
                + jobId + "_" + type + "_" + timestamp + ".png"));
        try {
            mediaFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaFile;
    }

    public static String getTimestamp() {
        return new SimpleDateFormat("MMdd_HHmmssSSS").format(new Date());
    }

    public static Drawable getDrawable(int resId, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(resId, context.getTheme());
        } else {
            return context.getResources().getDrawable(resId);
        }
    }

    public static void disableItem(View view) {
        view.setEnabled(false);
        view.setClickable(false);
        view.setFocusable(false);
    }

    public static Drawable getColorDrawable(Context context, int resId) {
        int color = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(context, resId);
        } else {
            color = context.getResources().getColor(resId);
        }
        return new ColorDrawable(color);
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
}

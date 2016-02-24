package com.codez.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Claudiu on 2015.03.03..
 */
public class SignatureView extends View {
    private static final float STROKE_WIDTH = 5f;

    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private final RectF dirtyRect = new RectF();
    private Paint paint = new Paint();
    private Path path = new Path();
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private float lastTouchX;
    private float lastTouchY;

    public SignatureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    public void clear() {
        path.reset();
        // Repaints the entire view.
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mBitmap == null) {
            Log.i("", "onSizeChanged BITMAP NULL");
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        }

        if (mCanvas == null) {
            mCanvas = new Canvas(mBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                // There is no end point yet, so don't waste cycles invalidating.
                return true;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                // Start tracking the dirty region.
                resetDirtyRect(eventX, eventY);

                // When the hardware tracks events faster than they are delivered, the
                // event will contain a history of those skipped points.
                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    expandDirtyRect(historicalX, historicalY);
                    path.lineTo(historicalX, historicalY);
                }

                // After replaying history, connect the line to the touch point.
                path.lineTo(eventX, eventY);
                break;

            default:
                //debug("Ignored touch event: " + event.toString());
                return false;
        }

        // Include half the stroke width to avoid clipping.
        invalidate(
                (int) (dirtyRect.left - HALF_STROKE_WIDTH),
                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
    }

    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    private void resetDirtyRect(float eventX, float eventY) {

        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }

    public byte[] getBitmap() {
        Bitmap bitmap;
        View v = this;
        v.setDrawingCacheEnabled(true);

        v.buildDrawingCache(true);

        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public Bitmap saveBitmap(String folderName, String pictureName) {
        Bitmap bitmap;
        View v = this;
        v.setDrawingCacheEnabled(true);

        v.buildDrawingCache(true);

        bitmap = Bitmap.createBitmap(v.getDrawingCache());

        File f = getSignaturePath(folderName, pictureName);
        try {
            f.delete();
            Log.d("SignatureView23", "createNewFile =" + f.createNewFile());
            FileOutputStream fo = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, fo);
            fo.close();
        } catch (Exception e) {
        }
        return bitmap;
    }

    public static File getSignaturePath(String folderName, String pictureName) {
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + folderName);
        if (!dir.exists())
            dir.mkdir();
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + folderName, pictureName);
        //Maybe add .png ???
    }
}

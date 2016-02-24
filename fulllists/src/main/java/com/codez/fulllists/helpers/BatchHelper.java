package com.codez.fulllists.helpers;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.codez.fulllists.model.SQLObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eptron on 12/29/2015.
 */
public class BatchHelper {

    private String mContentProviderAuthority;
    ArrayList<ContentProviderOperation> mBatch;
    private Context mContext;
    private OnSelectBuilderListner mSelectListener;
    private Uri mContentUri;

    public BatchHelper(Context context, String authority) {
        mContentProviderAuthority = authority;
        mBatch = new ArrayList<>();
        mContext = context;
    }

    public void setOnSelectBuilderListener(OnSelectBuilderListner selectListener) {
        mSelectListener = selectListener;
    }

    public void setContentUri(Uri uri) {
        mContentUri = uri;
    }

    public boolean addMultipleBatches(String idColumn, List<? extends SQLObject> list) {
        if (mContentUri != null) {
            addMultipleBatches(idColumn, list, mContentUri);
            return true;
        }
        return false;
    }

    public void addMultipleBatches(String idColumn, List<? extends SQLObject> list, Uri contentUri) {
        if (list != null && list.size() > 0) {
            int[] ids = getIds(list);
            String selectionString = buildSelectionString(idColumn, ids);
            String[] selectionArgs = buildSelectionArgs();
            String[] projection = buildProjection(idColumn);

            Cursor cursor = mContext.getContentResolver().query(
                    contentUri,
                    projection,
                    selectionString,
                    selectionArgs, null);

            List<Integer> updateIds = new ArrayList<>();
            while (cursor.moveToNext()) {
                updateIds.add(cursor.getInt(cursor.getColumnIndex(idColumn)));
            }
            cursor.close();

            for (int i = 0; i < ids.length; i++) {
                boolean exists;
                if (updateIds.contains(ids[i])) {
                    exists = true;
                } else {
                    exists = false;
                }
                if (exists && list.get(i).isDeleted()) {
                    addDeleteBatch(list.get(i), contentUri);
                } else {
                    addSimpleBatch(list.get(i), contentUri, exists);
                }
            }
        }
    }

    public void addSimpleBatch(SQLObject object, Uri contentUri, boolean exists) {
        if (object != null) {
            if (exists) {
                addUpdateBatch(object, contentUri);
            } else {
                addInsertBatch(object, contentUri);
            }
        }
    }

    public void bulkInsertBatch(List<? extends SQLObject> list, Uri contentUri) {
        if (list != null) {
            if (list.size() > 10) {
                ContentValues[] cvs = new ContentValues[list.size()];
                for (int i = 0; i < list.size(); i++)
                    cvs[i] = list.get(i).toContentValues();
                mContext.getContentResolver()
                        .bulkInsert(contentUri, cvs);
            } else {
                for (int i = 0; i < list.size(); i++)
                    addInsertBatch(list.get(i), contentUri);
            }
        }
    }

    public boolean addInsertBatch(SQLObject object) {
        if (mContentUri != null) {
            addInsertBatch(object, mContentUri);
            return true;
        }
        return false;
    }

    public void addInsertBatch(SQLObject object, Uri contentUri) {
        mBatch.add(ContentProviderOperation.newInsert(contentUri)
                .withValues(object.toContentValues()).build());
    }

    public boolean addUpdateBatch(SQLObject object) {
        if (mContentUri != null) {
            addUpdateBatch(object, mContentUri);
            return true;
        }
        return false;
    }

    public void addUpdateBatch(SQLObject object, Uri contentUri) {
        if (object != null) {
            int itemId = object.getId();
            Uri updateUri = contentUri.buildUpon()
                    .appendPath(Integer.toString(itemId)).build();
            mBatch.add(ContentProviderOperation.newUpdate(updateUri)
                    .withValues(object.toContentValues()).build());
        }
    }

    public boolean addDeleteBatch(SQLObject object) {
        if (mContentUri != null) {
            addDeleteBatch(object);
            return true;
        }
        return false;
    }

    public void addDeleteBatch(SQLObject object, Uri contentUri) {
        if (object != null) {
            int itemId = object.getId();
            Uri deleteUri = contentUri.buildUpon()
                    .appendEncodedPath(Integer.toString(itemId)).build();
            mBatch.add(ContentProviderOperation.newDelete(deleteUri).build());
        }
    }

    private int[] getIds(List<? extends SQLObject> list) {
        int[] ids = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i).getId();
        }
        return ids;
    }

    private String buildSelectionString(String idColumn, int[] ids) {

        String finalSelectionString = "";
        String idsString = QuerryHelper.buildSelectionString(idColumn, ids);
        if (mSelectListener != null) {
            finalSelectionString = mSelectListener.getSelection() + " AND ";
        }
        finalSelectionString = finalSelectionString + idsString;
        return finalSelectionString;
    }

    private String[] buildSelectionArgs() {
        if (mSelectListener != null) {
            return mSelectListener.getSelectionArgs();
        }
        return null;
    }

    private String[] buildProjection(String idColumn) {
        if (mSelectListener != null) {
            String[] retString = mSelectListener.getProjection(idColumn);
            if (retString != null)
                return retString;
        }
        return new String[]{idColumn};
    }

    public boolean executeBatches() {
        try {
            if (mBatch.size() > 0) {
                mContext.getContentResolver()
                        .applyBatch(mContentProviderAuthority, mBatch);
            } else {
                Log.i("BatchHelper", "No batches to apply...");
            }
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
            return false;
        }
        mBatch = new ArrayList<>();
        return true;
    }

    public interface OnSelectBuilderListner {
        String getSelection();

        String[] getSelectionArgs();

        String[] getProjection(String extraColumn);
    }
}

package com.codez.mainlibrary.restful_list.helpers;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.codez.mainlibrary.restful_list.SyncLogic;
import com.codez.mainlibrary.restful_list.model.SQLObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Eptron on 10/29/2015.
 */
public class SyncHelper {
    private SyncHelper(Context context) {
        mContext = context;
    }

    private String mName = "SH-";
    private Context mContext;
    private Uri mUri;
    private String mProviderAuthority;
    private int mExtraId = -1;
    private String mIdColumnName = "";
    private SyncLogic mCallback;

    public void setName(String name) {
        mName = "SH-" + name;
    }

    public void changeAuthority(String authority) {
        mProviderAuthority = authority;
    }

    public void setExtraId(int id) {
        mExtraId = id;
    }

    public void insertOrUpdateData(List<? extends SQLObject> listTemp, boolean isUpdate) {
        insertOrUpdateData(listTemp, isUpdate, mUri);
    }

    public void insertOrUpdateData(List<? extends SQLObject> listTemp, boolean isUpdate, Uri uri) {
        if (listTemp != null) {
            if (mCallback != null) {
                if (mCallback.doSyncLogic(listTemp, isUpdate, uri)) {
                    Log.i("SH-Custom", "CustomSync Successful");
                } else {
                    Log.i("SH-Custom", "CustomSync Failed");
                }
            } else {
                if (isUpdate) {
                    updateData(listTemp, uri);
                } else {
                    insertData(listTemp, uri);
                }
            }
        }
    }

    public void insertData(List<? extends SQLObject> list, Uri uri) {
        List<ContentValues> cvs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && !list.get(i).isDeleted())
                cvs.add(list.get(i).toContentValues());
        }
        ContentValues[] temp = new ContentValues[cvs.size()];
        for (int i = 0; i < cvs.size(); i++)
            temp[i] = cvs.get(i);
        mContext.getContentResolver().bulkInsert(uri, temp);
    }

    public void updateData(List<? extends SQLObject> listTemp, Uri uri) {
        String[] projection = setupProjection();
        String selection = null;
        String[] selectionArgs = null;
        BatchHelper batchHelper = new BatchHelper(mContext, mProviderAuthority);
        batchHelper.setContentUri(uri);
        if (projection.length > 1 && mExtraId > 0) {
            selection = mIdColumnName + " LIKE ?";
            selectionArgs = new String[]{mExtraId + ""};
        }
        if (listTemp.size() > 0) {
            Cursor currentData = null;//getCustomTimestampCursor();
            ContentResolver contentResolver = mContext.getContentResolver();
            if (currentData == null) {
                currentData = contentResolver.query(uri,
                        projection,
                        selection,
                        selectionArgs, null);
            }
            HashMap<Integer, SQLObject> entryMap = new HashMap<>();
            for (int i = 0; i < listTemp.size(); i++) {
                entryMap.put(listTemp.get(i).getId(), listTemp.get(i));
            }
            ArrayList<ContentProviderOperation> batch = new ArrayList<>();
            while (currentData.moveToNext()) {
                int id = currentData.getInt(currentData.getColumnIndex(BaseColumns._ID));
                SQLObject match = entryMap.get(id);
                if (match != null) {
                    entryMap.remove(id);
                    /*Uri updateUri = uri.buildUpon()
                            .appendPath(Integer.toString(id)).build();*/
                    if (match.isDeleted()) {
                        batchHelper.addDeleteBatch(match);
                        Log.i(mName, "Deleting item id=" + id);

                        //Delete
                       /* Uri deleteUri = uri.buildUpon()
                                .appendPath(Integer.toString(id)).build();
                        batch.add(ContentProviderOperation.newDelete(deleteUri).build());*/
                    } else {
                        batchHelper.addUpdateBatch(match);
                        Log.i(mName, "Updating item id=" + id);
                        //Update
                       /* ContentValues cv = match.toContentValues();
                        batch.add(ContentProviderOperation.newUpdate(updateUri)
                                .withValues(cv).build());*/
                    }
                }
            }
            currentData.close();
            for (SQLObject obj : entryMap.values()) {
                Log.i(mName, "Inserting new item id=" + obj.getId());
                if (!obj.isDeleted())
                    batchHelper.addInsertBatch(obj);
                    /*batch.add(ContentProviderOperation.newInsert(uri)
                            .withValues(obj.toContentValues()).build()); */
            }
            batchHelper.executeBatches();
            /*
            try {
                if (batch.size() > 0) {
                    contentResolver.applyBatch(mProviderAuthority, batch);
                }
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }*/
        } else {
            Log.i(mName, "No items to sync/update");
        }
    }

    private String[] setupProjection() {
        if (TextUtils.isEmpty(mIdColumnName))
            return new String[]{BaseColumns._ID};
        else return new String[]{BaseColumns._ID, mIdColumnName};
    }

    public static void customSyncItems(Context context, List<? extends SQLObject> list,
                                       boolean isUpdate, Uri uri, String providerName,
                                       int extraId, String extraIdName, String syncName) {
        if (list != null && !list.isEmpty()) {
            SyncHelper helper = SyncHelper.buildHelper(
                    context, isUpdate,
                    uri,
                    providerName,
                    extraId, extraIdName);
            helper.setName(syncName);
            helper.insertOrUpdateData(list, isUpdate);
        }
    }

    public static SyncHelper buildHelper(Context context, boolean isUpdate, Uri uri, String authority) {
        return buildHelper(context, isUpdate, uri, authority, null, null);
    }

    public static SyncHelper buildHelper(Context context, boolean isUpdate, Uri uri,
                                         String authority, Integer extraId, String columnName) {
        SyncHelper.Builder builder = new SyncHelper.Builder()
                .create(context)
                .setUri(uri);
        if (isUpdate) {
            builder.enableUpdate(authority);
            if (extraId != null && columnName != null) {
                builder.setCustomUpdate(extraId, columnName);
            }
        }
        return builder.build();
    }

    public static class Builder {
        private SyncHelper mHolder;

        public Builder create(Context context) {
            mHolder = new SyncHelper(context);
            return this;
        }

        public Builder setUri(Uri uri) {
            mHolder.mUri = uri;
            return this;
        }

        public Builder setLogName(String name) {
            mHolder.mName = name;
            return this;
        }

        public Builder enableUpdate(String authority) {
            mHolder.mProviderAuthority = authority;
            return this;
        }

        public Builder setCustomUpdate(int extraId, String columnName) {
            mHolder.mExtraId = extraId;
            mHolder.mIdColumnName = columnName;
            return this;
        }

        public Builder setCustomLogic(SyncLogic callback) {
            mHolder.mCallback = callback;
            return this;
        }

        public SyncHelper build() {
            return mHolder;
        }

    }
}

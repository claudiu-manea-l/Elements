package com.codez.mainlibrary.restful_list.lists;

import android.database.Cursor;

import com.codez.mainlibrary.restful_list.UpdateDataEvent;
import com.codez.mainlibrary.restful_list.helpers.SyncHelper;
import com.codez.mainlibrary.restful_list.model.UpdatableItem;

import de.greenrobot.event.EventBus;

/**
 * Created by Eptron on 10/15/2015.
 */
public abstract class UpdatableSQList extends RestfulSQLList {


    protected abstract String getContentAuthority();

    protected Cursor getCustomTimestampCursor() {
        return null;
    }

    @Override
    protected void checkData() {
        long timestamp = checkTimestamp();
        if (timestamp > 0) {
            isPulling = fetchData(timestamp);
        } else {
            super.checkData();
        }
    }

    protected long checkTimestamp() {
        long timestamp = -1;
        Cursor cur = getCustomTimestampCursor();
        if (cur == null) {
            cur = getActivity().getContentResolver().query(getSQLUri(),
                    UpdatableItem.TIMESTAMP_PROJ,
                    null, null, null);
        }
        if (cur != null && cur.moveToFirst()) {
            timestamp = cur.getLong(cur.getColumnIndex(UpdatableItem.MAX_STAMP));
            cur.close();
        }
        return timestamp;
    }

    protected boolean fetchData(long timeStamp) {
        return false;
    }

    @Override
    protected void initSyncHelper() {
        SyncHelper.Builder builder = new SyncHelper.Builder();
        builder
                .create(getActivity())
                .setUri(getSQLUri())
                .enableUpdate(getContentAuthority());
        if (hasCustomSyncLogic) {
            builder.setCustomLogic(mCallback);
        }
        mSyncHelper = builder.build();
    }

    public void onEventAsync(UpdateDataEvent event) {
        isPulling = false;
        FinishedInsertion insEvent = new FinishedInsertion();
        mSyncHelper.insertOrUpdateData(event.getData(), event.isUpdate());
        if (event.isUpdate()) {
            insEvent.setUpdate();
            //updateData(event.getData());
        } else {
            // insertIntoTable(event.getData());
        }
        EventBus.getDefault().post(insEvent);
    }
}

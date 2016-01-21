package com.codez.mainlibrary.restful_list;

import android.net.Uri;

import com.codez.mainlibrary.restful_list.model.SQLObject;

import java.util.List;

/**
 * Created by Eptron on 12/7/2015.
 */
public interface SyncLogic {
    boolean doSyncLogic(List<? extends SQLObject> listTemp, boolean isUpdate, Uri uri);
}

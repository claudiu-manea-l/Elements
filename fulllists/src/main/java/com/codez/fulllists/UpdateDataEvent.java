package com.codez.fulllists;

import com.codez.fulllists.model.SQLObject;
import com.codez.mainlibrary.restfulkit.model.MainEvent;

import java.util.List;

/**
 * Created by Eptron on 10/19/2015.
 */
public abstract class UpdateDataEvent extends MainEvent {
    public boolean isUpdate = false;

    public abstract List<? extends SQLObject> getData();

    public void setUpdate(){
        isUpdate = true;
    }

    public boolean isUpdate(){
        return isUpdate;
    }
}

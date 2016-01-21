package com.codez.mainlibrary.restfulkit.model;

/**
 * Created by eptron on 28/05/2015.
 */
public class SuccessfulRequest extends MainEvent {
    private String mSuccessMessage="";

    public void setMessage(String message){
        mSuccessMessage = message;
    }

    public String getMessage(){
        return mSuccessMessage;
    }
}

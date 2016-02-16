package com.codez.elements.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codez.elements.R;

import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Claudiu on 2/16/2016.
 */
public class RxTryActivity extends AppCompatActivity implements Observer<Integer> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_activity);
        Integer[] integerList = getIntegerList();
        Observable<Integer> myObservable = Observable.from(integerList);
        myObservable.subscribe(this);
        myObservable
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer < 10;
                    }
                });
               /* .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        Log.i("RxActivity", " --- finallyDo.call()");
                    }
                });*/

        myObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.i("Action1", " --- call(" + integer + ")");
            }
        });
        //myObservable.buffer(2);


    }

    private Integer[] getIntegerList() {
        Integer[] temp = new Integer[100];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = i;
        }
        return temp;
    }

    @Override
    public void onCompleted() {
        Log.i("RxActivity", " --- onCompleted() called");
    }

    @Override
    public void onError(Throwable e) {
        Log.i("RxActivity", " --- onError() called");
        e.printStackTrace();
    }

    @Override
    public void onNext(Integer integer) {
        Log.i("RxActivity", "onNext(" + integer + ")");
    }
}

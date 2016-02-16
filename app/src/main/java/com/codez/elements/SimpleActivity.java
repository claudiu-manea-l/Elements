package com.codez.elements;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.codez.elements.activities.ExpandableViewActivity;
import com.codez.elements.activities.RxListActivity;
import com.codez.elements.activities.RxTryActivity;
import com.codez.mainlibrary.BaseActivity;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import java.util.List;

/**
 * Created by Eptron on 1/25/2016.
 */
public class SimpleActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final int EXPANDABLE = 0;
    public static final int REBUILDABLE = 1;
    public static final int RESTFUL = 2;
    public static final int TITLED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.main_activity);
        initListView();

    }

    private void initListView() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        String[] items = getResources().getStringArray(R.array.list_items);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, android.R.id.text1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (position) {
            case EXPANDABLE:
                intent = new Intent(this, ExpandableViewActivity.class);
                break;
            case REBUILDABLE:
                intent = new Intent(this, RxTryActivity.class);
                break;
            case RESTFUL:
                intent = new Intent(this, RxListActivity.class);
                break;
            case TITLED:
                break;
        }
        if (intent != null)
            startActivity(intent);
    }
}

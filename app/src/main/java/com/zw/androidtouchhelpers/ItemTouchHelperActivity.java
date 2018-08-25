package com.zw.androidtouchhelpers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.zw.androidtouchhelpers.adapter.ItemTouchHelperAdapter;

public class ItemTouchHelperActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch_helper);

        recyclerView = findViewById(R.id.recycler_viewiew);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ItemTouchHelperAdapter adapter = new ItemTouchHelperAdapter(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        ItemTouchHelperAdapter.MyItemTouchHelperCallback touchHelperCallback
                = new ItemTouchHelperAdapter.MyItemTouchHelperCallback(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}

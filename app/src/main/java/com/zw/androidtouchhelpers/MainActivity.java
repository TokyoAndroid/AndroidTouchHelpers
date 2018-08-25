package com.zw.androidtouchhelpers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_gd).setOnClickListener(this);
        findViewById(R.id.btn_vdh).setOnClickListener(this);
        findViewById(R.id.btn_ith).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_gd:
                startActivity(new Intent(this,GestureDetectorActivity.class));
                break;
            case R.id.btn_vdh:
                startActivity(new Intent(this,ViewDragHelperActivity.class));
                break;
            case R.id.btn_ith:
                startActivity(new Intent(this,ItemTouchHelperActivity.class));
                break;
        }
    }
}

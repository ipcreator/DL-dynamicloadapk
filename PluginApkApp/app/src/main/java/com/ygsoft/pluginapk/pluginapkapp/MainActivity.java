package com.ygsoft.pluginapk.pluginapkapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.clickTxt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clickTxt) {
            Intent intent = new Intent(this, CustomViewProxyActivity.class);
            intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, "/mnt/sdcard/plugin.apk");
            startActivity(intent);
        }
    }
}

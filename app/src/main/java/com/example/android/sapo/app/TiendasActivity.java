package com.example.android.sapo.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class TiendasActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("name")) {
                this.setTitle("Almacenes de " + intent.getStringExtra("name"));
            }
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_tiendas, new TiendasFragment())
                    .commit();
        }
    }

}

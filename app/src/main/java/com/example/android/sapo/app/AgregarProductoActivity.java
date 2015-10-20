package com.example.android.sapo.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class AgregarProductoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_agregar_producto, new AgregarProductoFragment())
                    .commit();
        }
    }
}

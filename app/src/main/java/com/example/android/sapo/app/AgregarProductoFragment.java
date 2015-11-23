package com.example.android.sapo.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.sapo.app.webservices.PostAgregarProductos;

public class AgregarProductoFragment extends Fragment {

    private final String LOG_TAG = AgregarProductoFragment.class.getSimpleName();
    private String almacenID;
    private Integer categoriaID;
    private String categoriaNombre;

    public AgregarProductoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_agregar_producto, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra("almacenID")) {
                almacenID = intent.getStringExtra("almacenID");
            }
            if (intent.hasExtra("categoriaID")){
                categoriaID = intent.getIntExtra("categoriaID",0);
            }
            if (intent.hasExtra("categoriaNombre")){
                categoriaNombre = intent.getStringExtra("categoriaNombre");
            }
        }

        Button button = (Button) rootView.findViewById(R.id.agregar_producto_enviar);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                PostAgregarProductos postAgregarProductos = new PostAgregarProductos();
                String[] params = new String[5];
                //NOMBRE DEL PRODUCTO
                EditText editText = (EditText) rootView.findViewById(R.id.agregar_producto_nombre);
                params[0] = editText.getText().toString();

                //DESCRIPCIÓN DEL PRODUCTO
                editText = (EditText) rootView.findViewById(R.id.agregar_producto_descripcion);
                params[1] = editText.getText().toString();

                //CATEGORIA DEL PRODUCTO
                params[2] = categoriaID.toString();

                //CANTIDAD DEL PRODUCTO
                //editText = (EditText) rootView.findViewById(R.id.agregar_producto_stock);
                //params[3] = editText.getText().toString();

                //ALMACEN ID
                params[4] = almacenID;

                postAgregarProductos.execute(params);

                Context context = getActivity();
                Intent intent = new Intent(getActivity(), ProductosActivity.class)
                        .putExtra("categoriaID", categoriaID)
                        .putExtra("categoriaNombre", categoriaNombre)
                        .putExtra("almacenID", almacenID);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}

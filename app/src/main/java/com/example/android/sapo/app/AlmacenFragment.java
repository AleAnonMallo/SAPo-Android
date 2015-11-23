package com.example.android.sapo.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.sapo.app.adapters.AlmacenAdapter;
import com.example.android.sapo.app.datatypes.DataTienda;
import com.example.android.sapo.app.webservices.FetchAlmacenTask;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

public class AlmacenFragment extends Fragment {

    private AlmacenAdapter tiendasAdapter;

    public AlmacenFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_logout) {
            LoginManager.getInstance().logOut();
            AlmacenActivity almacenActivity = (AlmacenActivity) getActivity();
            almacenActivity.inflateLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<DataTienda> list = new ArrayList<DataTienda>();

        tiendasAdapter =
                new AlmacenAdapter(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_tiendas, // The name of the layout ID.
                        (ArrayList<DataTienda>) list);

        updateAlmacenes();

        View rootView = inflater.inflate(R.layout.fragment_tiendas, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_tiendas);
        listView.setAdapter(tiendasAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), CategoriasActivity.class)
                        .putExtra("almacenID", tiendasAdapter.getItem(i).getId())
                        .putExtra("almacenNombre", tiendasAdapter.getItem(i).getNombre());

                startActivity(intent);
            }
        });

        return rootView;
    }


    private void updateAlmacenes() {
        FetchAlmacenTask tiendasTask = new FetchAlmacenTask(getActivity(), tiendasAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        AlmacenActivity almacenActivity = (AlmacenActivity) getActivity();
        tiendasTask.execute(almacenActivity.getIdUsuario());
    }

    @Override
    public void onStart() {
        super.onStart();
        updateAlmacenes();
    }

}

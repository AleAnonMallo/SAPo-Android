package com.example.android.sapo.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.android.sapo.app.adapters.TiendaAdapter;
import com.example.android.sapo.app.datatypes.DataTienda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TiendasFragment extends Fragment {

    private TiendaAdapter tiendasAdapter;

    public TiendasFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<DataTienda> list = new ArrayList<DataTienda>();

        tiendasAdapter =
                new TiendaAdapter(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_tiendas, // The name of the layout ID.
                        (ArrayList<DataTienda>) list);

        FetchTiendasTask tiendasTask = new FetchTiendasTask();
        tiendasTask.execute();

        View rootView = inflater.inflate(R.layout.fragment_tiendas, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_tiendas);
        listView.setAdapter(tiendasAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getActivity();
                Intent intent = new Intent(getActivity(), CategoriasActivity.class)
                        .putExtra("almacenID", tiendasAdapter.getItem(i).getId())
                        .putExtra("almacenNombre", tiendasAdapter.getItem(i).getNombre());

                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchTiendasTask extends AsyncTask<Void, Void, DataTienda[]> {

        private final String LOG_TAG = FetchTiendasTask.class.getSimpleName();

        private DataTienda[] getAlmacenes(String JsonStr) throws JSONException {
            Log.v(LOG_TAG, "getAlmacenes");
            JSONArray aJson = new JSONArray(JsonStr);
            DataTienda[] resultStrs = new DataTienda[aJson.length()];
            for(int i = 0; i < aJson.length(); i++) {
                JSONObject oJson = aJson.getJSONObject(i);
                resultStrs[i] = new DataTienda();
                resultStrs[i].setNombre(oJson.getString("nombre"));
                resultStrs[i].setId((int) oJson.getInt("id"));
            }
            return  resultStrs;
        }

        @Override
        protected DataTienda[] doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonStr = null;

            try {
                final String SAPO_BASE_URL = "https://sapo.azure-api.net/sapo/almacenes";
                final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
                final String OCP_APIM_SUBSCRIPTION_VALUE = "9f86432ae415401db0383f63ce64c4fe";

                Uri builtUri = Uri.parse(SAPO_BASE_URL).buildUpon()
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Crea la conexión a Azure con la OCP_APIM_SUBSCRIPTION_KEY.
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty(OCP_APIM_SUBSCRIPTION_KEY, OCP_APIM_SUBSCRIPTION_VALUE);
                urlConnection.connect();

                // Lee el input stream
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Si no leyó nada, termina.
                    return null;
                }

                JsonStr = buffer.toString();
                Log.v(LOG_TAG, "JSON: " + JsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    //Cierra la conexión
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                //Parsea el JSON.
                return getAlmacenes(JsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(DataTienda[] result) {
            if (result != null) {
                tiendasAdapter.clear();
                for(DataTienda r : result) {
                    tiendasAdapter.add(r);
                }
            }
        }
    }
}

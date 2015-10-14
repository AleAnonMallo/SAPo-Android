package com.example.android.sapo.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

/**
 * Created by Alejandro on 13-Oct-15.
 */
public class ProductosFragment extends Fragment {

    private final String LOG_TAG = ProductosFragment.class.getSimpleName();
    private ArrayAdapter<String> productosAdapter;

    public ProductosFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        List<String> list = new ArrayList<String>();

        productosAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_productos, // The name of the layout ID.
                        R.id.list_item_producto,
                        list);

        View rootView = inflater.inflate(R.layout.fragment_productos, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_productos);
        listView.setAdapter(productosAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getActivity();
                String text = productosAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), ProductosActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, text);
                startActivity(intent);
            }
        });

        Intent intent = getActivity().getIntent();
        Integer text = 0;
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            text = intent.getIntExtra(Intent.EXTRA_TEXT,0);
        }

        FetchProductosTask fetchProductosTask = new FetchProductosTask();
        fetchProductosTask.execute((int) text);

        return rootView;
    }

    public class FetchProductosTask extends AsyncTask<Integer, Void, String[]> {

        private final String LOG_TAG = FetchProductosTask.class.getSimpleName();

        private String[] getProductos(String JsonStr) throws JSONException {
            JSONArray jsonArray = new JSONArray(JsonStr);
            String[] resultStrs = new String[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject producto = jsonArray.getJSONObject(i);
                resultStrs[i] = producto.getString("nombre");
            }
            return resultStrs;
        }

        @Override
        protected String[] doInBackground(Integer... integers) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonStr = null;

            try {
                final String SAPO_BASE_URL = "https://sapo.azure-api.net/sapo/almacenes/";
                final String SAPO_APPEND_URL1 = "productos";
                final String SAPO_APPEND_URL2 = "categoria";
                final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
                final String OCP_APIM_SUBSCRIPTION_VALUE = "9f86432ae415401db0383f63ce64c4fe";
                //final String ALMACENID_VALUE = integers[0].toString();
                final String ALMACENID_VALUE = "16";
                //final String CATEGORIAID_VALUE = integers[1].toString();
                final String CATEGORIAID_VALUE = "5";

                Uri builtUri = Uri.parse(SAPO_BASE_URL).buildUpon()
                        .appendPath(ALMACENID_VALUE)
                        .appendPath(SAPO_APPEND_URL1).appendPath(SAPO_APPEND_URL2)
                        .appendPath(CATEGORIAID_VALUE)
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
                return getProductos(JsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                productosAdapter.clear();
                for(String r : result) {
                    productosAdapter.add(r);
                }
            }
        }
    }
}

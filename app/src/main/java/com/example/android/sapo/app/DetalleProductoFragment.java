package com.example.android.sapo.app;

import android.app.Activity;
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
import android.widget.TextView;

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
 * Created by Alejandro on 14/10/2015.
 */
public class DetalleProductoFragment extends Fragment {

    private final String LOG_TAG = DetalleProductoFragment.class.getSimpleName();
    //private ArrayAdapter<String> productosAdapter;
    private Integer almacenID;
    private TextView textView;

    public DetalleProductoFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detalle_producto, container, false);
        Intent intent = getActivity().getIntent();


        Integer categoriaID = 0;
        String categoriaNombre = "";
        if (intent != null && intent.hasExtra("productoID")) {
            textView = (TextView) rootView.findViewById(R.id.text_detalle_producto);
            textView.setText("ID del producto: " + intent.getIntExtra("productoID",0));

            //categoriaID = intent.getIntExtra("categoriaID",0);
            //almacenID = intent.getIntExtra("almacenID", 0);
            //categoriaNombre = intent.getStringExtra("categoriaNombre");
        }


        Activity activity = getActivity();
        activity.setTitle(categoriaNombre);

        //FetchProductosTask fetchProductosTask = new FetchProductosTask();
        //fetchProductosTask.execute(param);

        return rootView;
    }

   /* public class FetchProductosTask extends AsyncTask<Integer, Void, String[]> {

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
                final String SAPO_APPEND_URL = "productos/categoria";
                final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
                final String OCP_APIM_SUBSCRIPTION_VALUE = "9f86432ae415401db0383f63ce64c4fe";
                final String ALMACENID_VALUE = integers[0].toString();
                final String CATEGORIAID_VALUE = integers[1].toString();

                Uri builtUri = Uri.parse(SAPO_BASE_URL).buildUpon()
                        .appendPath(ALMACENID_VALUE)
                        .appendEncodedPath(SAPO_APPEND_URL)
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
*/
}

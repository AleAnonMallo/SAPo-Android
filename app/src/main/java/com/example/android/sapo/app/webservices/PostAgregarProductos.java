package com.example.android.sapo.app.webservices;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alejandro on 20-Oct-15.
 */
public class PostAgregarProductos extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = PostAgregarProductos.class.getSimpleName();


    @Override
    protected Void doInBackground(String... strings) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String JsonStr = null;

        try {
            final String SAPO_BASE_URL = "https://sapo.azure-api.net/sapo/productos/create";

            final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
            final String OCP_APIM_SUBSCRIPTION_VALUE = "9f86432ae415401db0383f63ce64c4fe";

            final String PARAM_NOMBRE = strings[0];
            final String PARAM_DESCRIPCION = strings[1];
            final String PARAM_CATEGORIA = strings[2];

            Uri builtUri = Uri.parse(SAPO_BASE_URL).buildUpon()
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Crea la conexión a Azure con la OCP_APIM_SUBSCRIPTION_KEY.
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty(OCP_APIM_SUBSCRIPTION_KEY, OCP_APIM_SUBSCRIPTION_VALUE);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            String str =  "{\n" +
                    "\"nombre\":\"" + PARAM_NOMBRE + "\",\n" +
                    "\"descripcion\":\"" + PARAM_DESCRIPCION + "\",\n" +
                    "\"categoria\":\"" + PARAM_CATEGORIA + "\",\n" +
                    "\"isgenerico\":\"" + false + "\"\n" +
                    "}";
            byte[] outputInBytes = str.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();

            // Lee el input stream
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            urlConnection.connect();
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
            try {
                JSONObject jsonObject = new JSONObject(JsonStr);
                int idProd = jsonObject.getInt("id");
                agregarProducto(idProd, strings[4]);
            } catch (JSONException je) {

            }
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
        return null;

    }

    public String agregarProducto(int idProd, String idAlmacen){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String JsonStr = null;

        try {
            final String SAPO_BASE_URL = "https://sapo.azure-api.net/sapo/almacenes/";
            final String SAPO_APPEND_URL = "/agregarproductos";

            final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
            final String OCP_APIM_SUBSCRIPTION_VALUE = "9f86432ae415401db0383f63ce64c4fe";

            Uri builtUri = Uri.parse(SAPO_BASE_URL+idAlmacen+SAPO_APPEND_URL).buildUpon()
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Crea la conexión a Azure con la OCP_APIM_SUBSCRIPTION_KEY.
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty(OCP_APIM_SUBSCRIPTION_KEY, OCP_APIM_SUBSCRIPTION_VALUE);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            String str =  "[{\n" +
                    "\"productoID\":\"" + idProd + "\",\n" +
                    "\"cantidad\":\"" + 0 + "\"\n" +
                    "}]";

            byte[] outputInBytes = str.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();

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
        return null;
    }
}

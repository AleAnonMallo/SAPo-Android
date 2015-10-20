package com.example.android.sapo.app.webservices;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
            //final String PARAM_CATEGORIA = params[2];
            final String PARAM_CATEGORIA = "16";
            final String PARAM_ISGENERICO = strings[3];
            final String PARAM_ID = strings[4];

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
                    "\"isgenerico\":\"" + PARAM_ISGENERICO + "\",\n" +
                    "\"id\":\"" + PARAM_ID + "\"\n" +
                    "}";

            Log.e("¡FB!", "JSON: " + str);
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
            Log.v("¡FB!", "JSON: " + JsonStr);
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

        /*String prodID = crearProducto(strings);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String JsonStr = null;

        try {
            final String SAPO_BASE_URL = "https://sapo.azure-api.net/sapo/almacenes/";
            final String SAPO_APPEND_URL = "agregarproductos";

            final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
            final String OCP_APIM_SUBSCRIPTION_VALUE = "9f86432ae415401db0383f63ce64c4fe";

            final String PARAM_CANTIDAD = strings[0];
            final String PARAM_PRODUCTOID = strings[1];
            //final String PARAM_AVID = strings[2];
            final String PARAM_AVID = "16";

            Uri builtUri = Uri.parse(SAPO_BASE_URL).buildUpon()
                    .appendPath(strings[4])
                    .appendEncodedPath(SAPO_APPEND_URL)
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
                    "\"cantidad\":\"" + PARAM_CANTIDAD + "\",\n" +
                    "\"productoID\":\"" + PARAM_PRODUCTOID + "\",\n" +
                    "\"avID\":\"" + PARAM_AVID + "\"\n" +
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
            Log.v("¡FB!", "JSON: " + JsonStr);
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
        return null;*/
    }

    public String crearProducto(String[] params){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String JsonStr = null;

        try {
            final String SAPO_BASE_URL = "https://sapo.azure-api.net/sapo/productos/create";

            final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
            final String OCP_APIM_SUBSCRIPTION_VALUE = "9f86432ae415401db0383f63ce64c4fe";

            final String PARAM_NOMBRE = params[0];
            final String PARAM_DESCRIPCION = params[1];
            //final String PARAM_CATEGORIA = params[2];
            final String PARAM_CATEGORIA = "16";
            final String PARAM_ISGENERICO = params[3];
            final String PARAM_ID = params[4];

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
                    "\"isgenerico\":\"" + PARAM_ISGENERICO + "\",\n" +
                    "\"id\":\"" + PARAM_ID + "\"\n" +
                    "}";

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
            Log.v("¡FB!", "JSON: " + JsonStr);
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

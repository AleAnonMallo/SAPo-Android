package com.example.android.sapo.app.webservices;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.sapo.app.adapters.AlmacenAdapter;
import com.example.android.sapo.app.data.SAPoContract;
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
import java.util.Vector;

/**
 * Created by Alejandro on 15-Oct-15.
 */
public class FetchAlmacenTask extends AsyncTask<String, Void, DataTienda[]> {

    private final String LOG_TAG = FetchAlmacenTask.class.getSimpleName();
    private AlmacenAdapter mTiendasAdapter;
    private final Context mContext;

    public FetchAlmacenTask(Context context, AlmacenAdapter almacenAdapter) {
        mContext = context;
        mTiendasAdapter = almacenAdapter;

    }

    private DataTienda[] getAlmacenes(String JsonStr) throws JSONException {
        Log.v(LOG_TAG, "getAlmacenes");
        JSONArray aJson = new JSONArray(JsonStr);
        DataTienda[] resultStrs = new DataTienda[aJson.length()];
        Vector<ContentValues> cVVector = new Vector<ContentValues>(resultStrs.length);
        for(int i = 0; i < aJson.length(); i++) {
            JSONObject oJson = aJson.getJSONObject(i);
            resultStrs[i] = new DataTienda();
            resultStrs[i].setNombre(oJson.getString("nombre"));
            resultStrs[i].setId((int) oJson.getInt("id"));

            addAlmacen(oJson.getString("id"), resultStrs[i].getNombre(), oJson.getString("descripcion"), oJson.getString("url"));
        }
        return  resultStrs;
    }

    @Override
    protected DataTienda[] doInBackground(String... strings) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String JsonStr = null;

        try {
            final String SAPO_BASE_URL = "https://sapo.azure-api.net/sapo/usuarios/";
            final String SAPO_APPEND_URL = "almacenes/list";
            final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
            final String OCP_APIM_SUBSCRIPTION_VALUE = "9f86432ae415401db0383f63ce64c4fe";
            //final String USUARIO_ID = strings[0];
            final String USUARIO_ID = "alejandroanonmallo@gmail.com";

            Uri builtUri = Uri.parse(SAPO_BASE_URL).buildUpon()
                    .appendPath(USUARIO_ID)
                    .appendEncodedPath(SAPO_APPEND_URL)
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
            mTiendasAdapter.clear();
            for(DataTienda r : result) {
                mTiendasAdapter.add(r);
            }
        }
    }

    public long addAlmacen(String idAlmacen, String nomAlmacen, String descAlmacen, String urlAlmacen) {
        long locationId;

        // First, check if the almacen with this idAlmacen exists in the db
        Cursor almacenCursor = mContext.getContentResolver().query(
                SAPoContract.AlmacenEntry.CONTENT_URI,
                new String[]{SAPoContract.AlmacenEntry._ID},
                SAPoContract.AlmacenEntry.COLUMN_ALMACEN_ID + " = ?",
                new String[]{idAlmacen},
                null);

        if (almacenCursor.moveToFirst()) {
            int locationIdIndex = almacenCursor.getColumnIndex(SAPoContract.AlmacenEntry._ID);
            locationId = almacenCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues locationValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(SAPoContract.AlmacenEntry.COLUMN_ALMACEN_ID, idAlmacen);
            locationValues.put(SAPoContract.AlmacenEntry.COLUMN_ALMACEN_NOMBRE, nomAlmacen);
            locationValues.put(SAPoContract.AlmacenEntry.COLUMN_ALMACEN_DESCRIPCION, descAlmacen);
            locationValues.put(SAPoContract.AlmacenEntry.COLUMN_ALMACEN_URL, urlAlmacen);

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    SAPoContract.AlmacenEntry.CONTENT_URI,
                    locationValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }
        almacenCursor.close();
        return locationId;
    }
}
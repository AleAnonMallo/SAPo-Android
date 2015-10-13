package com.example.android.sapo.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alejandro on 12-Oct-15.
 */
public class DetailFragment extends Fragment {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    private TextView textView;

    public DetailFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FetchTiendaIdTask fetchTiendaIdTask = new FetchTiendaIdTask();
        fetchTiendaIdTask.execute();


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            /*((TextView) rootView.findViewById(R.id.detail_text))
                    .setText(text);*/
            textView = (TextView) rootView.findViewById(R.id.detail_text);
            textView.setText(text + "A!");
        }
        Log.v(LOG_TAG, "Setea el texto.");
        return rootView;
    }

    public class FetchTiendaIdTask extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchTiendaIdTask.class.getSimpleName();

        private String[] getAlmacenes(String JsonStr) throws JSONException {
            JSONObject oJson = new JSONObject(JsonStr);
            JSONArray categorias = oJson.getJSONArray("categorias");
            String[] resultStrs = new String[categorias.length()];

            for(int i = 0; i < categorias.length(); i++) {
                JSONObject categoria = categorias.getJSONObject(i);
                resultStrs[i] = categoria.getString("nombre");
            }
            return  resultStrs;
        }

        @Override
        protected String[] doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String JsonStr = null;

            try {
                final String SAPO_BASE_URL = "https://sapo.azure-api.net/sapo/almacenes";
                final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
                final String OCP_APIM_SUBSCRIPTION_VALUE = "9f86432ae415401db0383f63ce64c4fe";
                final String ALMACENID_VALUE = "2";

                Uri builtUri = Uri.parse(SAPO_BASE_URL).buildUpon()
                        .appendPath(ALMACENID_VALUE)
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
        protected void onPostExecute(String[] result) {
            textView.setText(result[0]+ "B!");
        }
    }
}

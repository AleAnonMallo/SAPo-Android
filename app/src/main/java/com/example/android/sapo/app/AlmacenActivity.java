package com.example.android.sapo.app;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.android.sapo.app.webservices.PostUsuario;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class AlmacenActivity extends ActionBarActivity {

    private CallbackManager callbackManager;
    private TextView info;
    private LoginButton loginButton;
    private String idUsuario;
    Bundle savedInstanceState2;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void inflateLogin() {
        setTitle("SAPo Login");
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState2 = savedInstanceState;

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        if (AccessToken.getCurrentAccessToken() == null){
            Log.v("¡FB!","ACCESS TOKEN ES NULL");

            setContentView(R.layout.activity_login);
            loginButton = (LoginButton)findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException e) {
                    Log.e("AlmacenActivity", e.toString());
                }

                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    Log.v("¡FB!", object.toString());
                                    PostUsuario postUsuario = new PostUsuario();
                                    String[] parametros = new String[4];
                                    try {
                                        idUsuario = object.getString("email");
                                        parametros[0] = idUsuario;
                                        parametros[1] = object.getString("last_name");
                                        parametros[2] = object.getString("first_name");
                                        parametros[3] = object.getString("id");
                                    } catch (JSONException ex) {

                                    } finally {
                                        postUsuario.execute(parametros);
                                        setContentView(R.layout.activity_tiendas);
                                        setTitle("Almacenes de " + parametros[2]);
                                        //if (savedInstanceState2 == null) {
                                            getSupportFragmentManager().beginTransaction()
                                                    .add(R.id.container_tiendas, new AlmacenFragment())
                                                    .commit();
                                        //}
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            });
        } else {
            Log.v("¡FB!","ACCESS TOKEN NOOO NULL. ELSE.");
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.v("¡FB!", object.toString());

                            PostUsuario postUsuario = new PostUsuario();
                            String[] parametros = new String[4];
                            try {
                                idUsuario = object.getString("email");
                                parametros[0] = idUsuario;
                                parametros[1] = object.getString("last_name");
                                parametros[2] = object.getString("first_name");
                                parametros[3] = object.getString("id");

                                setContentView(R.layout.activity_tiendas);
                                setTitle("Almacenes de " + parametros[2]);
                                //if (savedInstanceState2 == null) {
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.container_tiendas, new AlmacenFragment())
                                        .commit();
                            } catch (JSONException ex) {

                            } finally {
                                //postUsuario.execute(parametros);
                                //setContentView(R.layout.activity_tiendas);
                                //setTitle("Almacenes de " + parametros[2]);
                                //if (savedInstanceState2 == null) {
                                    //getSupportFragmentManager().beginTransaction()
                                      //      .add(R.id.container_tiendas, new AlmacenFragment())
                                        //    .commit();
                                //}
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

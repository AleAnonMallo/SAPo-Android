package com.example.android.sapo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState2 = savedInstanceState;

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        if (AccessToken.getCurrentAccessToken() == null){
            setContentView(R.layout.activity_login);

            loginButton = (LoginButton)findViewById(R.id.login_button);

            loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onCancel() {
                    info.setText("Login attempt cancelled.");
                }

                @Override
                public void onError(FacebookException e) {
                    info.setText("Login attempt failed.");
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
                                        /*Intent intent = new Intent(loginActivity, AlmacenActivity.class);
                                        intent.putExtra("email", parametros[0]);
                                        intent.putExtra("first_name", parametros[1]);
                                        intent.putExtra("last_name", parametros[2]);
                                        intent.putExtra("token", AccessToken.getCurrentAccessToken().getToken());
                                        startActivity(intent);*/
                                        setContentView(R.layout.activity_tiendas);
                                        setTitle(parametros[1]);
                                        if (savedInstanceState2 == null) {
                                            getSupportFragmentManager().beginTransaction()
                                                    .add(R.id.container_tiendas, new AlmacenFragment())
                                                    .commit();
                                        }
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
            Log.v("¡FB!", "ELSE2!");
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
                            } catch (JSONException ex) {

                            } finally {
                                postUsuario.execute(parametros);
                                /*Intent intent = new Intent(loginActivity, AlmacenActivity.class);
                                intent.putExtra("email", parametros[0]);
                                intent.putExtra("first_name", parametros[1]);
                                intent.putExtra("last_name", parametros[2]);
                                intent.putExtra("token", AccessToken.getCurrentAccessToken().getToken());
                                startActivity(intent);*/
                                setContentView(R.layout.activity_tiendas);
                                setTitle("Almacenes de " + parametros[2]);
                                if (savedInstanceState2 == null) {
                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.container_tiendas, new AlmacenFragment())
                                            .commit();
                                }
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }
}

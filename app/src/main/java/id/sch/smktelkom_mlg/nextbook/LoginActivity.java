package id.sch.smktelkom_mlg.nextbook;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private Button btLogin;
    private EditText etUsername, etPassword;
    private LinearLayout ll;
    private String username, password;
    private LoginButton fbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize prefs
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        ll = findViewById(R.id.LinearLayoutLogin);
        checkConn();
        TextView tvRegis = findViewById(R.id.textViewRegis);
        tvRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btLogin = findViewById(R.id.buttonLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPass);

        fbLogin = findViewById(R.id.buttonFBLogin);
        fbLogin.setHeight(100);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook", "Login Success, getting user info");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("Facebook", response.toString());
                                // Get facebook data from login
                                Bundle bFacebookData = getFacebookData(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancel() {
                Log.d("Facebook", "Login Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook", error.toString());
            }
        });
    }

    private void checkConn() {
        Snackbar snackbar = Snackbar
                .make(ll, "Connecting to server...", Snackbar.LENGTH_LONG);
        snackbar.show();

        final String url = Config.ServerURL + "login";
        Log.d("Volley", "Sending request to : " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            String code = res.getString("code");
                            String message = res.getString("message");
                            Integer codes = Integer.parseInt(code);
                            if (codes == 1) {
                                Snackbar snackbar = Snackbar
                                        .make(ll, message, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley", "Error : " + error.getMessage());
                Snackbar snackbar = Snackbar
                        .make(ll, "Failed connect to server", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        );
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    //From stackoverflow
    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            //Log.d(TAG,"Error parsing JSON");
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isValid() {
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("This field is required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("This field is required");
            return false;
        }
        return true;
    }

    private void doLogin() {
        if (isValid()) {
            String url = Config.ServerURL + "login";
            btLogin.setText("Please wait ..");
            btLogin.setEnabled(false);
            Log.d("Volley", "Sending request to : " + url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            btLogin.setText("Login");
                            btLogin.setEnabled(true);
                            try {
                                JSONObject res = new JSONObject(response);
                                Log.d("Volley", "Response : " + response);
                                String code = res.getString("code");
                                String message = res.getString("message");
                                Integer codes = Integer.parseInt(code);
                                if (codes == 1) {
                                    Prefs.putString("username", res.getString("username"));
                                    Prefs.putString("fullname", res.getString("fullname"));
                                    Prefs.putString("email", res.getString("email"));
                                    Prefs.putString("uid", res.getString("uid"));
                                    Prefs.putString("pict", res.getString("pict"));
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else if (codes == 2) {
                                    alert("Alert", message);
                                } else if (codes == 3) {
                                    alert("Alert", message);
                                }
                                Snackbar snackbar = Snackbar.make(ll, message, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            btLogin.setText("Login");
                            btLogin.setEnabled(true);
                            Log.d("Volley", "Error : " + error.toString());
                            Snackbar snackbar = Snackbar.make(ll, "An error occurred, try again later", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(postRequest);
        }
    }

    public void alert(String title, String message) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}

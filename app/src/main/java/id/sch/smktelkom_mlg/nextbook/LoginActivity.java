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
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

public class LoginActivity extends AppCompatActivity {
    private Button btLogin;
    private EditText etUsername, etPassword;
    private LinearLayout ll;
    private String username, password;

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

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            btLogin.setText("Login");
                            btLogin.setEnabled(true);
                            try {
                                JSONObject res = new JSONObject(response);
                                Log.d("Nganu", response);
                                String code = res.getString("code");
                                String message = res.getString("message");
                                Integer codes = Integer.parseInt(code);
                                if (codes == 1) {
                                    Prefs.putString("username", res.getString("username"));
                                    Prefs.putString("email", res.getString("email"));
                                    Prefs.putString("uid", res.getString("uid"));

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
                            // error
                            btLogin.setText("Login");
                            btLogin.setEnabled(true);
                            //Log.d("Error.Response", error.toString());
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

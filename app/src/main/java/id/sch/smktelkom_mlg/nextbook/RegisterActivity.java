package id.sch.smktelkom_mlg.nextbook;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etFullname, etEmail, etPass;
    private String fullname, username, email, pass;
    private Button btRegis;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ll = findViewById(R.id.linearLayout);
        etUsername = findViewById(R.id.editTextUsername);
        etFullname = findViewById(R.id.editTextFullname);
        etEmail = findViewById(R.id.editTextEmail);
        etPass = findViewById(R.id.editTextPass);
        btRegis = findViewById(R.id.buttonRegis);
        btRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegis();
            }
        });
    }

    private void doRegis() {
        if (isValid()) {
            String url = Config.ServerURL + "user";

            btRegis.setText("Registering, please wait ..");
            btRegis.setEnabled(false);

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            btRegis.setText("Register");
                            btRegis.setEnabled(true);
                            try {
                                JSONObject res = new JSONObject(response);
                                Log.d("Nganu", response);
                                String code = res.getString("code");
                                String message = res.getString("message");
                                Integer codes = Integer.parseInt(code);
                                if (codes == 4) {
                                    etUsername.setError(message);
                                } else if (codes == 3) {
                                    etEmail.setError(message);
                                } else if (codes == 1) {
                                    alert("Registration Success",
                                            "Registration successful, please check your email for account activation");
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
                            btRegis.setText("Register");
                            btRegis.setEnabled(true);
                            //Log.d("Error.Response", error.toString());
                            Snackbar snackbar = Snackbar.make(ll, "An error occurred, try again later", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("dspname", fullname);
                    params.put("username", username);
                    params.put("email", email);
                    params.put("password", pass);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(postRequest);
        }
    }

    public void alert(String title, String message) {
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        RegisterActivity.this.finish();
                    }
                }).show();
    }

    public boolean isValid() {
        fullname = etFullname.getText().toString().trim();
        username = etUsername.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        pass = etPass.getText().toString().trim();
        if (TextUtils.isEmpty(fullname)) {
            etFullname.setError("This field is required");
            return false;
        }
        if (fullname.length() < 5) {
            etFullname.setError("Minimal 5 karakter");
            return false;
        }
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("This field is required");
            return false;
        }
        if (username.length() < 5) {
            etUsername.setError("Minimal 5 karakter");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("This field is required");
            return false;
        }
        if (!email.matches("[a-zA-Z0-9._-]+@[a-z._-]+.[a-z]+")) {
            etEmail.setError("Invalid Email Address");
        }
        if (TextUtils.isEmpty(pass)) {
            etPass.setError("This field is required");
            return false;
        }
        if (pass.length() < 6) {
            etPass.setError("Minimal 6 karakter");
            return false;
        }
        return true;
    }
}

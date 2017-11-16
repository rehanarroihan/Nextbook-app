package id.sch.smktelkom_mlg.nextbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import id.sch.smktelkom_mlg.nextbook.Util.Config;

public class LoginActivity extends AppCompatActivity {
    private Button btLogin;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    private void doLogin() {
        if (isValid()) {
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            StringRequest sendData = new StringRequest(Request.Method.POST, Config.ServerURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    private boolean isValid() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
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
}

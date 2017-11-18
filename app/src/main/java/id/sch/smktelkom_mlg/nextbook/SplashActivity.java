package id.sch.smktelkom_mlg.nextbook;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final TextView status = findViewById(R.id.textViewStatus);

        //Initialize prefs
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        String url = Config.ServerURL + "login";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Nganu", response);
                            String code = res.getString("code");
                            String message = res.getString("message");
                            Integer codes = Integer.parseInt(code);
                            if (codes == 1) {
                                status.setText(message);
                                String username = Prefs.getString("username", null);
                                if (username == null) {
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        status.setText("Connection error, please try again later");
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(postRequest);
    }
}

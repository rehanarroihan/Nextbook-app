package id.sch.smktelkom_mlg.nextbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private Button btJoin;
    private EditText etClassCode;
    private ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Join Class");
        etClassCode = findViewById(R.id.editTextClassCode);
        btJoin = findViewById(R.id.buttonJoinClass);
        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classCode = etClassCode.getText().toString();
                if (classCode.length() != 7) {
                    etClassCode.setError("Please enter valid class code");
                } else {
                    checkCode(classCode);
                }
            }
        });
    }

    public void scan(View view) {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void handleResult(Result result) {
        etClassCode.setText(result.getText());
        Log.d("Scanner", result.getText());
    }

    private void checkCode(String classcode) {
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ScannerActivity.this);
        progressDoalog.setMessage("Checking class code ...");
        progressDoalog.show();

        String url = Config.ServerURL + "aclass/checkcode?code=" + classcode + "&uid=" + Prefs.getString("uid", null);
        Log.d("Volley", "Sending request to : " + url);
        StringRequest reqs = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            String message = res.getString("message");
                            Integer codes = res.getInt("code");
                            if (codes == 1) {
                                MainActivity.MA.finish();
                                progressDoalog.dismiss();
                                Toast.makeText(ScannerActivity.this,
                                        message, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ScannerActivity.this, MainActivity.class));
                                finish();
                            } else if (codes == 2) {
                                progressDoalog.dismiss();
                                Toast.makeText(ScannerActivity.this,
                                        message, Toast.LENGTH_LONG).show();
                            } else {
                                progressDoalog.dismiss();
                                Toast.makeText(ScannerActivity.this,
                                        message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.d("Volley", "Error : " + e.toString());
                progressDoalog.dismiss();
                Toast.makeText(ScannerActivity.this,
                        e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(reqs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}

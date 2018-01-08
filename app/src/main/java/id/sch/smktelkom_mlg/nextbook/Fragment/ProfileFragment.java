package id.sch.smktelkom_mlg.nextbook.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private EditText etFullname, etUsername, etEmail;
    private CircleImageView ivProfile;
    private LinearLayout llLoading, llEdit;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etFullname = getView().findViewById(R.id.textViewFullnameEdit);
        etUsername = getView().findViewById(R.id.textViewUsernameEdit);
        etEmail = getView().findViewById(R.id.textViewEmailEdit);
        ivProfile = getView().findViewById(R.id.imageViewUserEdit);
        llLoading = getView().findViewById(R.id.linearLayoutLoading);
        llEdit = getView().findViewById(R.id.linearLayoutEdit);
        loadData();
    }

    private void loadData() {
        String url = Config.ServerURL + "login/useredit?uid=" + Prefs.getString("uid", null);
        Log.d("Volley", "Sending request to : " + url);
        StringRequest reqs = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            etFullname.setText(res.getString("dspname"));
                            etUsername.setText(res.getString("username"));
                            etEmail.setText(res.getString("email"));
                            String provider = res.getString("prov");
                            String imgurl = Config.ImageURL + "2.0/img/user/" + res.getString("pict");
                            Glide.with(getActivity()).load(imgurl).into(ivProfile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        llLoading.setVisibility(View.GONE);
                        llEdit.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.d("Volley", "Error : " + e.toString());
                e.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(reqs);
    }
}

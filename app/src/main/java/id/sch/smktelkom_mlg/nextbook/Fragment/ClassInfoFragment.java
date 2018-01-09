package id.sch.smktelkom_mlg.nextbook.Fragment;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.nextbook.Adapter.MemberAdapter;
import id.sch.smktelkom_mlg.nextbook.Model.Member;
import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.SplashActivity;
import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

public class ClassInfoFragment extends Fragment {
    ArrayList<Member> mList = new ArrayList<>();
    MemberAdapter mAdapter;
    RecyclerView rvMember;
    private Button btUnen;
    private TextView btCopyCode;
    private ImageView ivQR;

    public ClassInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Prefs.Builder()
                .setContext(getActivity())
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getActivity().getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_info, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final TextView tvClassName = getView().findViewById(R.id.textViewGroupName);
        final TextView tvClassDesc = getView().findViewById(R.id.textViewGroupDesc);
        final TextView tvClassMember = getView().findViewById(R.id.textViewMember);
        final ImageView ivClass = getView().findViewById(R.id.imageViewGroup);
        ivQR = getView().findViewById(R.id.imageViewQR);
        Glide.with(getActivity())
                .load(Config.ServerURL + "genqr?text=" + Prefs.getString("classid", null) + "&size=150")
                .into(ivQR);
        rvMember = getView().findViewById(R.id.recyclerViewMember);
        btUnen = getView().findViewById(R.id.buttonUnenroll);
        btUnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Apakah anda yakin ingin keluar kelas ini ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        unenroll();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        TextView tvQR = getView().findViewById(R.id.textViewClassCode);
        tvQR.setText(Prefs.getString("classid", null));
        btCopyCode = getView().findViewById(R.id.buttonCopyCode);
        btCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(Prefs.getString("classid", null));
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", Prefs.getString("classid", null));
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(getActivity(), "Code di copy ke clipboard", Toast.LENGTH_LONG).show();
            }
        });

        String url = Config.ServerURL + "aclass/classinfo?cid=" + Prefs.getString("classid", null);
        Log.d("Volley", "Sending request to : " + url);
        StringRequest reqss = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            tvClassName.setText(res.getString("class_name"));
                            tvClassDesc.setText(res.getString("class_descr"));
                            String memCount = String.valueOf(res.getString("member"));
                            tvClassMember.setText(memCount + " Member");
                            Glide.with(getActivity())
                                    .load(Config.ImageURL + "2.0/img/group/" + res.getString("groupimg"))
                                    .into(ivClass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(reqss);

        loadMemberList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvMember.setLayoutManager(layoutManager);
        rvMember.setNestedScrollingEnabled(false);
        mAdapter = new MemberAdapter(getContext(), mList);
        rvMember.setAdapter(mAdapter);
    }

    private void unenroll() {
        String url = Config.ServerURL + "aclass/unenroll";
        Log.d("Volley", "Sending request to : " + url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            String code = res.getString("code");
                            Integer codes = Integer.parseInt(code);
                            if (codes == 1) {
                                startActivity(new Intent(getActivity(), SplashActivity.class));
                                getActivity().finish();
                            } else if (codes == 2) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", "Error : " + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", Prefs.getString("uid", null));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    private void loadMemberList() {
        //Load member list
        mList.clear();
        String urls = Config.ServerURL + "aclass/getmember?cid=" + Prefs.getString("classid", null);
        Log.d("Volley", "Sending request to : " + urls);
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET, urls, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", "Response : " + response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                Member member = new Member();
                                member.setName(data.getString("name"));
                                member.setPp(data.getString("pp"));
                                member.setEmail(data.getString("email"));
                                member.setProv(data.getString("prov"));
                                member.setPps(data.getString("pps"));
                                mList.add(member);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", "Error : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(reqData);
    }

    @Override
    public void onStop() {
        super.onStop();
        AppController mQ = new AppController();
        if (mQ != null) {
            mQ.cancelAllRequest(getActivity());
        }
    }
}

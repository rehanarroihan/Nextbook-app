package id.sch.smktelkom_mlg.nextbook.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.ScannerActivity;
import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

/**
 * A simple {@link Fragment} subclass.
 */

public class ClassFragment extends Fragment {
    public TabLayout tabLayout;
    public ViewPager viewPager;

    private FloatingActionMenu fab;
    private ImageView ivNoClass;

    public ClassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fab = getView().findViewById(R.id.menu);
        ivNoClass = getView().findViewById(R.id.imageViewNoClass);
        FloatingActionButton fabCreate = getView().findViewById(R.id.menu_item);
        FloatingActionButton fabJoin = getView().findViewById(R.id.menu_item1);
        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nClassDialog();
            }
        });
        fabJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                startActivity(intent);
            }
        });

        tabLayout = getView().findViewById(R.id.tabs);
        viewPager = getView().findViewById(R.id.viewpager);
        viewPager.setAdapter(new SectionsPagerAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        getActivity().setTitle("Loading...");
        isHaveClass();
    }

    public void nClassDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogv = inflater.inflate(R.layout.newclass_layout, null);
        dialogBuilder.setView(dialogv);
        dialogBuilder.setTitle("Buat Kelas Baru");
        dialogBuilder.setMessage("Isikan form yang tersedia");

        final EditText etClName = dialogv.findViewById(R.id.editTextClassName);
        final EditText etClDesc = dialogv.findViewById(R.id.editTextClassDesc);
        final String cname = etClName.getText().toString();
        final String cdesc = etClDesc.getText().toString();
        Button btCreate = dialogv.findViewById(R.id.buttonCreateClass);
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(cname) && !TextUtils.isEmpty(cdesc)) {
                    Log.d("Class Fragment", "Clicked " + cname + " " + cdesc);
                    createClass(cname, cdesc);
                } else {

                }
            }
        });
//        dialogBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                Log.d("Class Fragment", "Clicked " + cname + " " + cdesc);
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void createClass(final String cname, final String cdesc) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Membuat kelas");
        dialog.show();

        String url = Config.ServerURL + "aclass";
        Log.d("Volley", "Sending request to : " + url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            Integer code = res.getInt("code");
                            if (code == 1) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Berhasil membuat kelas",
                                        Toast.LENGTH_LONG).show();
                                getActivity().finish();
                                startActivity(getActivity().getIntent());
                            } else if (code == 2) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Gagal membuat kelas",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Gagal membuat kelas",
                                Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", Prefs.getString("uid", null));
                params.put("name", cname);
                params.put("descript", cdesc);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    public void isHaveClass() {
        String url = Config.ServerURL + "aclass/ishave?id=" + Prefs.getString("uid", null);
        Log.d("Volley", "Sending request to : " + url);
        StringRequest reqs = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            String code = res.getString("code");
                            Integer codes = Integer.parseInt(code);
                            if (codes == 1) {
                                Prefs.putString("classid", res.getString("classid"));
                            } else {
                                getActivity().setTitle("Class");
                            }
                            doSomething(codes);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.d("Volley", "Error : " + e.toString());
                e.printStackTrace();
                Config ehe = new Config();
                ehe.timeout(getActivity());
                getActivity().finish();
            }
        });
        AppController.getInstance().addToRequestQueue(reqs);
    }

    private void doSomething(Integer codes) {
        Log.d("Code : ", codes.toString());
        if (codes == 1) {
            Log.d("ClassFragment : ", "Have class");
            fab.setVisibility(View.GONE);
            ivNoClass.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);

            String url = Config.ServerURL + "aclass/classinfo?cid=" + Prefs.getString("classid", null);
            Log.d("Volley", "Sending request to : " + url);
            StringRequest reqss = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response);
                                Log.d("Volley", "Response : " + response);
                                getActivity().setTitle(res.getString("class_name"));
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
        } else if (codes == 2) {
            Log.d("ClassFragment : ", "Dont have class");
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            ivNoClass.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        AppController mQ = new AppController();
        if (mQ != null) {
            mQ.cancelAllRequest(getActivity());
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new ClassStreamFragment();
            if (position == 1)
                return new ClassScheduleFragment();
            if (position == 2)
                return new ClassInfoFragment();
            else
                return new ClassStreamFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Stream";
                case 1:
                    return "Schedule";
                case 2:
                    return "Class Info";
            }
            return null;
        }
    }

}

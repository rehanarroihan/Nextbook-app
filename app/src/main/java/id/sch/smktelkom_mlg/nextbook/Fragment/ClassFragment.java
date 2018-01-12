package id.sch.smktelkom_mlg.nextbook.Fragment;


import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.ScannerActivity;
import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

/**
 * A simple {@link Fragment} subclass.
 */

public class ClassFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    private FloatingActionMenu fab;
    private ImageView ivNoClass;

    public ClassFragment() {
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
        final View dialogView = inflater.inflate(R.layout.newclass_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText etClName = dialogView.findViewById(R.id.editTextClassName);
        final EditText etClDesc = dialogView.findViewById(R.id.editTextClassDesc);

        dialogBuilder.setTitle("Buat Kelas Baru");
        dialogBuilder.setMessage("Masukkan form yang tersedia");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
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

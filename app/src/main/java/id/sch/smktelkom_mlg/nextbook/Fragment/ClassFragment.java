package id.sch.smktelkom_mlg.nextbook.Fragment;


import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        FloatingActionButton fabJoin = getView().findViewById(R.id.menu_item1);
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
        isHaveClass();
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
                            }
                            doSomethingBitch(codes);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void doSomethingBitch(Integer codes) {
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1)
                return new ClassStreamFragment();
            else
                return new ClassStreamFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Steam";
                case 1:
                    return "Schedule";
                case 2:
                    return "Member";
            }
            return null;
        }
    }

}

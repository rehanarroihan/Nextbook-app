package id.sch.smktelkom_mlg.nextbook.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.sch.smktelkom_mlg.nextbook.Adapter.ScheduleAdapter;
import id.sch.smktelkom_mlg.nextbook.Model.Schedule;
import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

public class ClassScheduleFragment extends Fragment {
    ArrayList<Schedule> mList = new ArrayList<>();

    Unbinder unbinder;
    @BindView(R.id.swipeRefreshSch)
    SwipeRefreshLayout srSch;

    @BindView(R.id.textViewN1)
    TextView tvN1;
    @BindView(R.id.textViewN2)
    TextView tvN2;
    @BindView(R.id.textViewN3)
    TextView tvN3;
    @BindView(R.id.textViewN4)
    TextView tvN4;
    @BindView(R.id.textViewN5)
    TextView tvN5;
    @BindView(R.id.textViewN6)
    TextView tvN6;
    @BindView(R.id.textViewN7)
    TextView tvN7;

    @BindView(R.id.recyclerViewSenin)
    RecyclerView rvSenin;
    @BindView(R.id.recyclerViewSelas)
    RecyclerView rvSelas;
    @BindView(R.id.recyclerViewRabu)
    RecyclerView rvRabu;
    @BindView(R.id.recyclerViewKamis)
    RecyclerView rvKamis;
    @BindView(R.id.recyclerViewJumat)
    RecyclerView rvJumat;
    @BindView(R.id.recyclerViewSabtu)
    RecyclerView rvSabtu;
    @BindView(R.id.recyclerViewMinggu)
    RecyclerView rvMinggu;

    @BindView(R.id.linearLayoutSc)
    LinearLayout llSc;
    @BindView(R.id.linearLayoutLoadingSch)
    LinearLayout llLoading;

    ScheduleAdapter mAdapter;

    private Integer prm = 0; //max 7
    private String[] day;
    private TextView[] tvn;
    private RecyclerView[] rv;

    private boolean onprogress;

    public ClassScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_schedule, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        srSch.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initSchedule();
            }
        });
        srSch.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void initSchedule() {
        Log.d("Volley", "Param : " + String.valueOf(prm));
        day = new String[]{"senin", "selasa", "rabu", "kamis", "jumat", "sabtu", "minggu"};
        tvn = new TextView[]{tvN1, tvN2, tvN3, tvN4, tvN5, tvN6, tvN7};
        rv = new RecyclerView[]{rvSenin, rvSelas, rvRabu, rvKamis, rvJumat, rvSabtu, rvMinggu};
        if (prm <= 6) {
            haveschedule();
        } else if (prm == 7) {
            srSch.setRefreshing(false);
            llLoading.setVisibility(View.GONE);
            llSc.setVisibility(View.VISIBLE);
            prm = 0;
        }
    }

    private void haveschedule() {
        String url = Config.ServerURL + "aclass/schedulecount?cid="
                + Prefs.getString("classid", null) + "&day=" + day[prm];
        Log.d("Volley", "Sending request to : " + url);
        StringRequest reqss = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            String code = res.getString("code");
                            Integer codes = Integer.parseInt(code);
                            if (codes == 2) {
                                Log.d("Volley", day[prm] + " ada jadwal");
                                loadSchedule();
                            } else {
                                Log.d("Volley", day[prm] + " tidak ada jadwal");
                                tvn[prm].setVisibility(View.VISIBLE);
                                tvn[prm].setText("TIDAK ADA JADWAL");
                                rv[prm].setVisibility(View.GONE);
                                if (prm != 7) {
                                    prm++;
                                    initSchedule();
                                }
                            }
                            Log.d("Volley", "Response : " + response);
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
    }

    private void loadSchedule() {
        mList.clear();
        String urls = Config.ServerURL + "aclass/schedule?cid=" + Prefs.getString("classid", null) + "&day=" + day[prm];
        Log.d("Volley", "Sending request to : " + urls);
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET, urls, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", "Response : " + response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                Schedule schedule = new Schedule();
                                schedule.setScheduleid(data.getString("scheduleid"));
                                schedule.setClassid(data.getString("classid"));
                                schedule.setLessonid(data.getString("lessonid"));
                                schedule.setDay(data.getString("day"));
                                schedule.setStart(data.getString("start"));
                                schedule.setEnd(data.getString("end"));
                                schedule.setLesson(data.getString("lesson"));
                                schedule.setTeacher(data.getString("teacher"));
                                mList.add(schedule);

                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                rv[prm].setLayoutManager(layoutManager);
                                rv[prm].setNestedScrollingEnabled(false);
                                mAdapter = new ScheduleAdapter(getContext(), mList);
                                rv[prm].setAdapter(mAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        prm++;
                        initSchedule();
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
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
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

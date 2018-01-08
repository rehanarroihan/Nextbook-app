package id.sch.smktelkom_mlg.nextbook.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

/**
 * A simple {@link Fragment} subclass.
 */

public class ClassScheduleFragment extends Fragment {
    ArrayList<Schedule> seninList = new ArrayList<>();
    ArrayList<Schedule> selasaList = new ArrayList<>();
    ArrayList<Schedule> rabuList = new ArrayList<>();
    ArrayList<Schedule> kamisList = new ArrayList<>();
    ArrayList<Schedule> jumatList = new ArrayList<>();
    ArrayList<Schedule> sabtuList = new ArrayList<>();
    ArrayList<Schedule> mingguList = new ArrayList<>();

    Unbinder unbinder;
    @BindView(R.id.recyclerViewSenin)
    RecyclerView rvSenin;
    @BindView(R.id.recyclerViewSelasa)
    RecyclerView rvSelasa;
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

    @BindView(R.id.indeterminateBar)
    ProgressBar pb;

    ScheduleAdapter mAdapter;

    public ClassScheduleFragment() {
        // Required empty public constructor
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
        String[] day = {"senin", "selasa", "rabu", "kamis", "jumat", "sabtu", "minggu"};
        RecyclerView[] rv = {rvSenin, rvSelasa, rvRabu, rvKamis, rvJumat, rvSabtu, rvMinggu};
        ArrayList[] arday = {seninList, selasaList, rabuList, kamisList, jumatList, sabtuList, mingguList};

//        for (int i = 0; i < day.length; i++) {
//            String dayname = day[i];
//            RecyclerView rvnow = rv[i];
//            ArrayList arnow = arday[i];
//            loadSchedule(dayname, arnow, rvnow);
//        }

        loadSchedule();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvSenin.setLayoutManager(layoutManager);
        mAdapter = new ScheduleAdapter(getContext(), seninList);
        rvSenin.setAdapter(mAdapter);
    }

    private void loadSchedule() {
        //Load member list
        String urls = Config.ServerURL + "aclass/schedule?cid="
                + Prefs.getString("classid", null) + "&day=senin";
        Log.d("Volley", "Sending request to : " + urls);
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET, urls, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", "Response : " + response);
                        for (int p = 0; p < response.length(); p++) {
                            try {
                                JSONObject data = response.getJSONObject(p);
                                Log.d("Code", data.getString("code"));
                                Schedule schedule = new Schedule();
                                schedule.setStart(data.getString("start"));
                                schedule.setEnd(data.getString("end"));
                                schedule.setLesson(data.getString("lesson"));
                                schedule.setTeacher(data.getString("teacher"));
                                seninList.add(schedule);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }
}

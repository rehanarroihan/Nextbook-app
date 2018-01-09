package id.sch.smktelkom_mlg.nextbook.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassStreamFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.textViewLessonNow)
    TextView tvLessonNow;
    @BindView(R.id.textViewNextLesson)
    TextView tvNextLesson;

    //Initialize new post box
    @BindView(R.id.imageViewNewPostUser)
    CircleImageView ivNPostUser;
    @BindView(R.id.textViewNewPostUser)
    TextView tvNPostUser;
    @BindView(R.id.textViewNPostLesson)
    TextView tvNPostLesson;
    @BindView(R.id.editTextPost)
    EditText etNPost;
    @BindView(R.id.buttonPost)
    Button btPost;

    private String lessonnow = "";
    private Integer lncode = 0;

    public ClassStreamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_stream, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url = Config.ServerURL + "aclass/lessonnow?cid=" + Prefs.getString("classid", null);
        Log.d("Volley", "Sending request to : " + url);
        StringRequest reqs = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            Integer codes = res.getInt("code");
                            tvLessonNow.setText(res.getString("lesson"));
                            String hehe = res.getString("nextlesson") + " " + res.getString("nextlessonTime");
                            tvNextLesson.setText(hehe);
                            lncode = res.getInt("lessonid");
                            if (codes == 1) {
                                lessonnow = res.getString("lesson");
                                tvNPostLesson.setText(lessonnow);
                            } else {
                                tvNPostLesson.setText("Other");
                            }
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
        tvNPostUser.setText(Prefs.getString("fullname", null));
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

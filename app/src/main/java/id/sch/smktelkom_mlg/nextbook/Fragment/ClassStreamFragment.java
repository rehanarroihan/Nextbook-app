package id.sch.smktelkom_mlg.nextbook.Fragment;


import android.content.Intent;
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
import id.sch.smktelkom_mlg.nextbook.Adapter.PostAdapter;
import id.sch.smktelkom_mlg.nextbook.Model.Post;
import id.sch.smktelkom_mlg.nextbook.NpostActivity;
import id.sch.smktelkom_mlg.nextbook.PostvActivity;
import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

/**
 * A simple {@link Fragment} subclass.
 */

public class ClassStreamFragment extends Fragment implements PostAdapter.IPostAdapter {
    ArrayList<Post> postList = new ArrayList<>();
    PostAdapter pAdapter;

    Unbinder unbinder;
    @BindView(R.id.textViewLessonNow)
    TextView tvLessonNow;
    @BindView(R.id.textViewNextLesson)
    TextView tvNextLesson;

    @BindView(R.id.linearLayoutLoadingStr)
    LinearLayout llLoad;
    @BindView(R.id.linearLayoutPost)
    LinearLayout llPost;
    @BindView(R.id.recyclerViewPost)
    RecyclerView rvPost;
    @BindView(R.id.fabnew)
    android.support.design.widget.FloatingActionButton fabnew;

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
        fabnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NpostActivity.class));
            }
        });
        loadData();
    }

    private void loadData() {
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
                            } else {
                                lessonnow = "Other";
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
        loadUserPost();
    }

    private void loadUserPost() {
        postList.clear();
        String url = Config.ServerURL + "aclass/postlist?cid=" + Prefs.getString("classid", null);
        Log.d("Volley", "Sending request to : " + url);
        JsonArrayRequest rez = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", "Response : " + response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                Post post = new Post();
                                post.setPostid(data.getString("postid"));
                                post.setUid(data.getString("uid"));
                                post.setDspname(data.getString("dspname"));
                                post.setPict(data.getString("pict"));
                                post.setLesson(data.getString("lesson"));
                                post.setCreate(data.getString("create"));
                                post.setContent(data.getString("content"));
                                post.setImg(data.getString("img"));
                                post.setDoc(data.getString("doc"));
                                post.setComment(data.getInt("comment"));
                                postList.add(post);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        rvPost.setLayoutManager(layoutManager);
                        rvPost.setNestedScrollingEnabled(false);
                        pAdapter = new PostAdapter(ClassStreamFragment.this.getActivity(), postList, ClassStreamFragment.this);
                        rvPost.setAdapter(pAdapter);

                        llLoad.setVisibility(View.GONE);
                        llPost.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", "Error : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(rez);
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

    @Override
    public void doClick(int pos) {
        Intent i = new Intent(getContext(), PostvActivity.class);
        i.putExtra("postid", postList.get(pos).getPostid());
        i.putExtra("pp", postList.get(pos).getPict());
        i.putExtra("uid", postList.get(pos).getUid());
        i.putExtra("create", postList.get(pos).getCreate());
        i.putExtra("dspname", postList.get(pos).getDspname());
        i.putExtra("lesson", postList.get(pos).getLesson());
        i.putExtra("content", postList.get(pos).getContent());
        i.putExtra("comment", String.valueOf(postList.get(pos).getComment()));
        i.putExtra("foto", postList.get(pos).getImg());
        i.putExtra("file", postList.get(pos).getDoc());
        startActivity(i);
    }
}

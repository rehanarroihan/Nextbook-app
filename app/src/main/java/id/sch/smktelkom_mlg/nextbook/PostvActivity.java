package id.sch.smktelkom_mlg.nextbook;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.sch.smktelkom_mlg.nextbook.Adapter.CommentAdapter;
import id.sch.smktelkom_mlg.nextbook.Model.Comment;
import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

public class PostvActivity extends AppCompatActivity
        implements CommentAdapter.ICommentAdapter {
    ArrayList<Comment> cList = new ArrayList<>();
    CommentAdapter mAdapter;

    @BindView(R.id.imageViewPostUserV)
    CircleImageView ivUser;
    @BindView(R.id.textViewPostUserV)
    TextView tvUser;
    @BindView(R.id.textViewPostLessonV)
    TextView tvLesson;
    @BindView(R.id.textViewPostCreateV)
    TextView tvCreate;
    @BindView(R.id.textViewContentV)
    TextView tvContent;
    @BindView(R.id.recyclerViewComment)
    RecyclerView rvComment;
    @BindView(R.id.editTextComment)
    EditText etComment;
    @BindView(R.id.coor)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.relativeLayoutImg)
    RelativeLayout rlImg;
    @BindView(R.id.relativeLayoutAttach)
    RelativeLayout rlFile;
    @BindView(R.id.textViewFoto)
    TextView tvFoto;
    @BindView(R.id.textViewAttach)
    TextView tvFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postv);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Kiriman");

        //Initialize prefs
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        final String file = getIntent().getStringExtra("file");
        final String foto = getIntent().getStringExtra("foto");
        if (!file.equals("N")) {
            tvFile.setText(getIntent().getStringExtra("file"));
            rlFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = Config.ImageURL + "2.0/file/doc/" + file;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        } else {
            rlFile.setVisibility(View.GONE);
        }
        if (!foto.equals("N")) {
            tvFoto.setText(foto);
            rlImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = Config.ImageURL + "2.0/file/img/" + foto;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        } else {
            rlImg.setVisibility(View.GONE);
        }

        Glide.with(this).load(getIntent().getStringExtra("pp")).into(ivUser);
        tvUser.setText(getIntent().getStringExtra("dspname"));
        tvLesson.setText(getIntent().getStringExtra("lesson"));
        tvCreate.setText(getIntent().getStringExtra("create"));
        tvContent.setText(getIntent().getStringExtra("content"));
        String cmncnt = getIntent().getStringExtra("comment");

        if (Integer.valueOf(cmncnt) > 0) {
            loadComment();
        }

        etComment.setHint("Komentar sebagai " + Prefs.getString("fullname", null));
        etComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    goComment();
                    return false;
                }
                return false;
            }
        });
    }

    private void goComment() {
        String url = Config.ServerURL + "aclass/comment";
        etComment.setEnabled(false);
        Log.d("Volley", "Sending request to : " + url);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            Integer codes = res.getInt("code");
                            if (codes == 1) {
                                etComment.setText("");
                                etComment.setEnabled(true);
                                loadComment();
                            } else if (codes == 2) {
                                etComment.setEnabled(true);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Tidak bisa mengirim komentar", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        etComment.setEnabled(true);
                        Log.d("Volley", "Error : " + error.toString());
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "An error occurred, try again later", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("postid", getIntent().getStringExtra("postid"));
                params.put("uid", Prefs.getString("uid", null));
                params.put("classid", Prefs.getString("classid", null));
                params.put("content", etComment.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    private void loadComment() {
        cList.clear();
        String url = Config.ServerURL + "aclass/commentlist?postid=" + getIntent().getStringExtra("postid");
        Log.d("Volley", "Sending request to : " + url);
        JsonArrayRequest rez = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", "Response : " + response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                Comment comment = new Comment();
                                comment.setCommentid(data.getString("commentid"));
                                comment.setUid(data.getString("uid"));
                                comment.setCreateds(data.getString("createds"));
                                comment.setComment(data.getString("comment"));
                                comment.setPict(data.getString("pict"));
                                comment.setDspname(data.getString("dspname"));
                                cList.add(comment);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(PostvActivity.this);
                        rvComment.setLayoutManager(layoutManager);
                        rvComment.setNestedScrollingEnabled(false);
                        mAdapter = new CommentAdapter(PostvActivity.this, cList);
                        rvComment.setAdapter(mAdapter);
                        rvComment.setVisibility(View.VISIBLE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (getIntent().getStringExtra("uid").equals(Prefs.getString("uid", null))) {
            MenuItem delete = menu.findItem(R.id.action_delete_post);
            delete.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.action_delete_post) {
            deletePost();
        }
        return true;
    }

    private void deletePost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah anda yakin akan menghapus kiriman anda ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dltPost();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void dltPost() {
        String url = Config.ServerURL + "aclass/post?postid=" + getIntent().getStringExtra("postid");
        Log.d("Volley", "Sending request to : " + url);
        StringRequest reqs = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            Integer codes = res.getInt("code");
                            if (codes == 1) {
                                Toast.makeText(getApplicationContext(), "Kiriman berhasil di hapus",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Gagal menghapus postingan", Snackbar.LENGTH_LONG);
                                snackbar.show();
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
    }

    @Override
    public void doDelete(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah anda yakin akan menghapus komentar anda ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteCmn(cList.get(pos).getCommentid());
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteCmn(String commentid) {
        String url = Config.ServerURL + "aclass/comment?commentid=" + commentid;
        Log.d("Volley", "Sending request to : " + url);
        StringRequest reqs = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            Log.d("Volley", "Response : " + response);
                            Integer codes = res.getInt("code");
                            if (codes == 1) {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Berhasil menghapus komentar", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                loadComment();
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Gagal menghapus komentar", Snackbar.LENGTH_LONG);
                                snackbar.show();
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
    }
}

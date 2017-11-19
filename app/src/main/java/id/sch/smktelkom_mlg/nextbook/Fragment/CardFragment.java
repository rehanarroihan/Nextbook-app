package id.sch.smktelkom_mlg.nextbook.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.nextbook.Adapter.CardAdapter;
import id.sch.smktelkom_mlg.nextbook.Model.Card;
import id.sch.smktelkom_mlg.nextbook.R;
import id.sch.smktelkom_mlg.nextbook.Util.AppController;
import id.sch.smktelkom_mlg.nextbook.Util.Config;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment {
    ArrayList<Card> mList = new ArrayList<>();
    CardAdapter mAdapter;
    RecyclerView rvCard;

    public CardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fab = getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        rvCard = getView().findViewById(R.id.recyclerViewCard);

        loadData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvCard.setLayoutManager(layoutManager);
        mAdapter = new CardAdapter(getContext(), mList);
        rvCard.setAdapter(mAdapter);
    }

    private void loadData() {
        String url = Config.ServerURL + "card?uid=" + Prefs.getString("uid", null);
        Log.d("Volley", "Sending request to : " + url);
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", "Response : " + response);
                        Log.d("Nganu", response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                Card card = new Card();
                                card.setCard_id(data.getString("card_id"));
                                card.setUid(data.getString("uid"));
                                card.setCard_name(data.getString("card_name"));
                                card.setCard_desc(data.getString("card_desc"));
                                card.setColor(data.getString("color"));
                                card.setCard_dt(data.getString("card_dt"));
                                card.setStatus(data.getString("status"));
                                mList.add(card);
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
}

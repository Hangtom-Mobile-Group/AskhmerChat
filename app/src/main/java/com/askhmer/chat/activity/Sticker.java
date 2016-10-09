package com.askhmer.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.adapter.StrickerBodyAdapter;
import com.askhmer.chat.adapter.StrickerHeaderAdapter;
import com.askhmer.chat.listener.AddStrickerToChat;
import com.askhmer.chat.listener.ClickListener;
import com.askhmer.chat.listener.RecyclerItemClickListenerInFragment;
import com.askhmer.chat.model.BodyStricker;
import com.askhmer.chat.model.HeaderStricker;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.JsonConverter;

import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Sticker extends Fragment {

    private List<HeaderStricker> headerStrickers = new ArrayList<HeaderStricker>();
    private List<BodyStricker> bodyStrickers = new ArrayList<BodyStricker>();
    private StrickerHeaderAdapter strickerHeaderAdapter;
    private StrickerBodyAdapter strickerBodyAdapter;
    private RecyclerView recyclerView, recyclerViewGrid;
    private AddStrickerToChat addStrickerToChat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.fragment_sticker, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_header);
        recyclerViewGrid = (RecyclerView) v.findViewById(R.id.recycler_view_body);

        setupDataFromServer();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), 4);
        recyclerViewGrid.setLayoutManager(gridLayout);
        recyclerViewGrid.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGrid.addOnItemTouchListener(new RecyclerItemClickListenerInFragment(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BodyStricker bodyStricker = bodyStrickers.get(position);
                addStrickerToChat.addStrickerToConversation(bodyStricker.getAlbumImage());
            }

            @Override
            public void onLongClick(final View view, final int position) {

            }
        }));


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListenerInFragment(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HeaderStricker headerStricker =headerStrickers.get(position);
                requestStkerItem(headerStricker.getId());
            }

            @Override
            public void onLongClick(final View view, final int position) {

            }
        }));

        return v;
    }

    private void setupDataFromServer() {
        try {
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.GET, API.LISTSTRICKER, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("STATUS") == 200) {
                            headerStrickers = new JsonConverter().toList(response.getJSONArray("DATA").toString(), HeaderStricker.class);
                        }
                    } catch (JSONException e) {

                    } finally {
                        strickerHeaderAdapter = new StrickerHeaderAdapter(headerStrickers);
                        strickerHeaderAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(strickerHeaderAdapter);
                        requestStkerItem(headerStrickers.get(0).getId());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
        }
        catch (Exception e) {

        }
    }

    private void requestStkerItem(String id) {
        try {
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.GET, API.LISTSTRICKERWITHID + "/" + id, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("STATUS") == 200) {
                            bodyStrickers = new JsonConverter().toList(response.getJSONArray("DATA").toString(), BodyStricker.class);
                        }
                    } catch (JSONException e) {

                    } finally {
                        strickerBodyAdapter = new StrickerBodyAdapter(bodyStrickers);
                        strickerBodyAdapter.notifyDataSetChanged();
                        recyclerViewGrid.setAdapter(strickerBodyAdapter);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
        }
        catch (Exception e) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.addStrickerToChat = (AddStrickerToChat) context;
    }
}

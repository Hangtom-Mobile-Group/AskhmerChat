package com.askhmer.chat.fragments;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.Chat;
import com.askhmer.chat.activity.UserProfile;
import com.askhmer.chat.adapter.FriendAdapter;
import com.askhmer.chat.listener.ClickListener;
import com.askhmer.chat.listener.RecyclerItemClickListenerInFragment;
import com.askhmer.chat.model.Friends;

import java.util.ArrayList;
import java.util.List;


public class OneFragment extends Fragment {


    private List<Friends> friendtList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FriendAdapter fAdapter;
    private LinearLayout firstShow;


    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareAddfriendData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View oneFragmentView = inflater.inflate(R.layout.fragment_one, container, false);

        setHasOptionsMenu(true);


        recyclerView = (RecyclerView) oneFragmentView.findViewById(R.id.recycler_view);
        firstShow = (LinearLayout) oneFragmentView.findViewById(R.id.layout_first_friend);

        fAdapter = new FriendAdapter(friendtList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fAdapter);

        recyclerView
                .addOnItemTouchListener(new RecyclerItemClickListenerInFragment(getActivity(), recyclerView, new com.askhmer.chat.listener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Friends fri = friendtList.get(position);
                        Toast.makeText(getActivity(), fri.getFriName() + " is selected!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));


        if( friendtList.size()==0){
            firstShow.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            firstShow.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        return oneFragmentView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      // inflater.inflate(R.menu.menu_friend, menu);
       // super.onCreateOptionsMenu(menu, inflater);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_friend, menu);


        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        super.onCreateOptionsMenu(menu, inflater);
    }





    private void prepareAddfriendData() {

        for(int i=0;i<=20;i++) {
            Friends friend = new Friends();
            friend.setFriName("Torn Longdy");
            friend.setChatId("xyz123hangtom");
            friendtList.add(friend);

        }
        /// fAdapter.notifyDataSetChanged();

    }



}

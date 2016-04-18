package com.askhmer.chat.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.AddfriendAdapter;
import com.askhmer.chat.listener.ClickListener;
import com.askhmer.chat.listener.RecyclerItemClickListenerInFragment;
import com.askhmer.chat.model.Friends;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longdy on 3/30/2016.
 */
public class ThreeFragment extends Fragment {
    private List<Friends> addfriendtList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AddfriendAdapter fAdapter;

    public ThreeFragment() {
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


        View threeFragmentView = inflater.inflate(R.layout.fragment_three, container, false);

        setHasOptionsMenu(true);


        recyclerView = (RecyclerView) threeFragmentView.findViewById(R.id.recycler_view_addfriend);

        fAdapter = new AddfriendAdapter(addfriendtList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListenerInFragment(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Friends fri = addfriendtList.get(position);
                Toast.makeText(getActivity(), fri.getFriName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return threeFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_addfriend, menu);


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
            Friends addfriend = new Friends();
            addfriend.setFriName("Lim Ravy");
            addfriend.setChatId("xyz123hangtom");
            addfriendtList.add(addfriend);

        }
        /// fAdapter.notifyDataSetChanged();
    }

}

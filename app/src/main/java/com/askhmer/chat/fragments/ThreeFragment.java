package com.askhmer.chat.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.InviteBySMS;
import com.askhmer.chat.activity.MainActivityTab;
import com.askhmer.chat.activity.SearchByID;
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

    private View searchbyid;
    private View invitebysms;

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View threeFragmentView = inflater.inflate(R.layout.fragment_three, container, false);

        searchbyid = threeFragmentView.findViewById(R.id.searchbyid);
        invitebysms = threeFragmentView.findViewById(R.id.invitebysms);
        searchbyid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"searchbyid",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SearchByID.class);
                startActivity(intent);
            }
        });
        invitebysms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"invitebysms",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), InviteBySMS.class);
                ((MainActivityTab)getActivity()).startActivity(intent);
            }
        });


        setHasOptionsMenu(true);
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

}

package com.askhmer.chat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.ContactAdapter;
import com.askhmer.chat.listener.ClickListener;
import com.askhmer.chat.listener.RecyclerItemClickListenerInFragment;
import com.askhmer.chat.model.Contact;

import java.util.ArrayList;
import java.util.List;


public class OneFragment extends Fragment {

    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter mAdapter;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View oneFragmentView = inflater.inflate(R.layout.fragment_one, container, false);


        recyclerView = (RecyclerView) oneFragmentView.findViewById(R.id.recycler_view);

        mAdapter = new ContactAdapter(contactList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
       // recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
       // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //list item
        for (int i = 0; i < 15; i++) {
            Contact item = new Contact("Friend : "+i,"Chat Id : 000"+i);
            contactList.add(item);
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListenerInFragment(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position)    {
                Contact movie = contactList.get(position);
                Toast.makeText(getActivity(), movie.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
/*

        final Toolbar toolbar = (Toolbar) oneFragmentView.findViewById(R.id.toolbar);

        recyclerView.setOnScrollListener(new MyScrollListener(getContext()) {
            @Override
            public void onMoved(int distance) {
                toolbar.setTranslationY(-distance);
            }
        });
*/

//        prepareMovieData();
        return oneFragmentView;
    }


    private void prepareMovieData() {
/*        Contact contact = new Contact("Mad Max: Fury Road", "Action & Adventure");
        contactList.add(contact);
        contact = new Contact("Inside Out", "Animation, Kids & Family");
        contactList.add(contact);

        contact = new Contact("Shaun the Sheep", "Animation");
        contactList.add(contact);

        contact = new Contact("The Martian", "Science Fiction & Fantasy");
        contactList.add(contact);

        contact = new Contact("Mission: Impossible Rogue Nation", "Action");
        contactList.add(contact);

        contact = new Contact("Up", "Animation");
        contactList.add(contact);

        contact = new Contact("Star Trek", "Science Fiction");
        contactList.add(contact);

        contact = new Contact("The LEGO Movie", "Animation");
        contactList.add(contact);

        contact = new Contact("Iron Man", "Action & Adventure");
        contactList.add(contact);

        contact = new Contact("Aliens", "Science Fiction");
        contactList.add(contact);

        contact = new Contact("Chicken Run", "Animation");
        contactList.add(contact);

        contact = new Contact("Back to the Future", "Science Fiction");
        contactList.add(contact);

        contact = new Contact("Raiders of the Lost Ark", "Action & Adventure");
        contactList.add(contact);

        contact = new Contact("Goldfinger", "Action & Adventure");
        contactList.add(contact);

        contact = new Contact("Guardians of the Galaxy", "Science Fiction & Fantasy");
        contactList.add(contact);*/

        mAdapter.notifyDataSetChanged();
    }

}

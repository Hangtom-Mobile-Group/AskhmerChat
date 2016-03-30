package com.askhmer.chat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.adapter.ContactAdapter;
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





        recyclerView.addOnItemTouchListener(new OneFragment.RecyclerTouchListener(getActivity(), recyclerView, new OneFragment.ClickListener() {
            @Override
            public void onClick(View view, int position)    {
                Contact movie = contactList.get(position);
                Toast.makeText(getActivity(), movie.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();
        return oneFragmentView;
    }


    private void prepareMovieData() {
        Contact contact = new Contact("Mad Max: Fury Road", "Action & Adventure");
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
        contactList.add(contact);

        mAdapter.notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private OneFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OneFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}

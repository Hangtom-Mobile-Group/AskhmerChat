package com.askhmer.chat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askhmer.chat.R;

public class TwoFragment extends Fragment {

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Tab", "Tab2");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

}

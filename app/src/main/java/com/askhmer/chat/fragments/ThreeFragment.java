package com.askhmer.chat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askhmer.chat.R;

/**
 * Created by Longdy on 3/30/2016.
 */
public class ThreeFragment extends Fragment {

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View threeFragmentView = inflater.inflate(R.layout.fragment_three, container, false);

        return threeFragmentView;
    }
}

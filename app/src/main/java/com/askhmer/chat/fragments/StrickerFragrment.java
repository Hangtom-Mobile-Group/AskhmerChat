package com.askhmer.chat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askhmer.chat.R;

/**
 * Created by soklundy on 5/5/2016.
 */
public class StrickerFragrment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stricker, container, false);
        return v;
    }
}

package com.askhmer.chat.introFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askhmer.chat.R;

/**
 * Created by soklundy on 4/19/2016.
 */
public class IntroTwo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_intro_two, container, false);
        return v;
    }
}

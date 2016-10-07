package com.askhmer.chat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.askhmer.chat.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    private String displayText;

    public BlankFragment(String displayText) {
        // Required empty public constructor
        this.displayText = displayText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView textView =  (TextView) v.findViewById(R.id.textView7);
        textView.setText(displayText);
        return v;
    }

}

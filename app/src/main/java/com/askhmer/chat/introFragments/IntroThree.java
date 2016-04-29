package com.askhmer.chat.introFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.askhmer.chat.R;
import com.askhmer.chat.util.MutiLanguage;

/**
 * Created by soklundy on 4/19/2016.
 */
public class IntroThree extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragrament_intro_three, container, false);

        final RadioGroup toggle = (RadioGroup) v.findViewById(R.id.radio_language);
        final int selectedId = toggle.getCheckedRadioButtonId();
        RadioButton radioBtnEn = (RadioButton) v.findViewById(R.id.radio_english);
        RadioButton radioBtnKm = (RadioButton) v.findViewById(R.id.radio_khmer);

        final MutiLanguage mutiLanguage = new MutiLanguage(getContext(),getActivity());
        String lang = mutiLanguage.getLanguageCurrent();

        if (lang.equals("en") || lang.isEmpty()) {
            radioBtnEn.setChecked(true);
        }else {
            radioBtnKm.setChecked(true);
        }
        return v;
    }
}

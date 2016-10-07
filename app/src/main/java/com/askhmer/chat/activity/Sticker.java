package com.askhmer.chat.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.askhmer.chat.R;
import com.askhmer.chat.fragments.BlankFragment;
import com.askhmer.chat.util.StickerViewPager;

import java.util.ArrayList;
import java.util.List;

public class Sticker extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.fragment_sticker, null);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        StickerViewPager adapter = new StickerViewPager(getFragmentManager());

        List<Fragment> fragments = new ArrayList<Fragment>();
        for (int i = 1; i < 5; i++) {
            fragments.add(new BlankFragment(i+""));
        }
        adapter.addFragment(fragments);
        viewPager.setAdapter(adapter);
    }
}

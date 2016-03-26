package com.askhmer.chat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.SecretChat;
import com.github.clans.fab.FloatingActionMenu;
public class TwoFragment extends Fragment  implements View.OnClickListener{

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close;

    private com.github.clans.fab.FloatingActionButton fab12;
    private com.github.clans.fab.FloatingActionButton fab22;

    private View hideLayout;


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

        View twoFragmentView = inflater.inflate(R.layout.fragment_two, container, false);

        hideLayout = (View) twoFragmentView.findViewById(R.id.hiden_layout);

        final FloatingActionMenu menu2 = (FloatingActionMenu) twoFragmentView.findViewById(R.id.menu2);

        fab12 = (com.github.clans.fab.FloatingActionButton) twoFragmentView.findViewById(R.id.fab12);
        fab22 = (com.github.clans.fab.FloatingActionButton) twoFragmentView.findViewById(R.id.fab22);

        menu2.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    hideLayout.setVisibility(View.VISIBLE);
                } else {
                    hideLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        fab12.setOnClickListener(this);
        fab22.setOnClickListener(this);



/*
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);

        fab = (FloatingActionButton) twoFragmentView.findViewById(R.id.fab);
        fab1 = (FloatingActionButton)twoFragmentView.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)twoFragmentView.findViewById(R.id.fab2);
*/
/*
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
*/

        // Inflate the layout for this fragment
        return twoFragmentView;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        String text = "";
        switch (id){
/*            case R.id.fab:

                animateFAB();
                break;
            case R.id.fab1:

                Log.d("fab", "Fab 1");
                break;
            case R.id.fab2:

                Log.d("fab", "Fab 2");
                break;
*/
            case R.id.fab12:
                Intent in = new Intent(getActivity(), SecretChat.class);
                startActivity(in);
                break;
            case R.id.fab22:
                text = fab22.getLabelText();
                break;
        }
    }
/*
    public void animateFAB(){

        if(isFabOpen)
        {
//            fab.startAnimation(rotate_forward);
            rotateFabBackward();
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("fab", "close");
        }
        else
        {
//            fab.startAnimation(rotate_backward);
            rotateFabForward();
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("fab","open");
        }
    }
*/

/*
    public void rotateFabForward() {
        ViewCompat.animate(fab)
                .rotation(45.0F)
                .withLayer()
                .setDuration(300L)
                .setInterpolator(new OvershootInterpolator(5.0F))
                .start();
    }

    public void rotateFabBackward() {
        ViewCompat.animate(fab)
                .rotation(0.0F)
                .withLayer()
                .setDuration(300L)
                .setInterpolator(new OvershootInterpolator(5.0F))
                .start();
    }
*/

}

package com.askhmer.chat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.Chat;
import com.askhmer.chat.activity.GroupChat;
import com.askhmer.chat.activity.SecretChat;
import com.askhmer.chat.adapter.SecretChatRecyclerAdapter;
import com.askhmer.chat.listener.ClickListener;
import com.askhmer.chat.listener.RecyclerItemClickListenerInFragment;
import com.askhmer.chat.model.Friends;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

public class TwoFragment extends Fragment  implements View.OnClickListener{

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close;

    private Button chatNow;

    private com.github.clans.fab.FloatingActionButton fab12;
    private com.github.clans.fab.FloatingActionButton fab22;

    private View hideLayout;

    private RecyclerView mRecyclerView;
    private int position;
    private ArrayList<Friends> mFriends;
    private LinearLayout firstShow;

    private FrameLayout fragment_tow_layout;

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

        hideLayout = twoFragmentView.findViewById(R.id.hiden_layout);
//        fragment_tow_layout = (FrameLayout) twoFragmentView.findViewById(R.id.fragment_tow_layout);

        final FloatingActionMenu menu2 = (FloatingActionMenu) twoFragmentView.findViewById(R.id.menu2);

        fab12 = (com.github.clans.fab.FloatingActionButton) twoFragmentView.findViewById(R.id.fab12);
        fab22 = (com.github.clans.fab.FloatingActionButton) twoFragmentView.findViewById(R.id.fab22);



        fab12.setOnClickListener(this);
        fab22.setOnClickListener(this);

        chatNow = (Button) twoFragmentView.findViewById(R.id.btn_chat_now);

        chatNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SecretChat.class);
                startActivity(in);
            }
        });

        mRecyclerView = (RecyclerView) twoFragmentView.findViewById(R.id.list_chat);
        firstShow = (LinearLayout) twoFragmentView.findViewById(R.id.layout_first);

        menu2.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    hideLayout.setVisibility(View.VISIBLE);
//                    fragment_tow_layout.setBackgroundColor(Color.parseColor("#82000000"));
                } else {
                    hideLayout.setVisibility(View.INVISIBLE);

//                    fragment_tow_layout.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        // Bind adapter to recycler
        mFriends = new ArrayList<>();


        //list item
        for (int i = 0; i < 15; i++) {
            Friends item = new Friends();
           item.setFriName("Chat room : " + i);
            item.setImg(R.drawable.profile);
            item.setChatId("chat Id : 000" + i);
            item.setIsOnline(true);
            mFriends.add(item);
        }



//        Log.d("item",item.getFriName());

        if( mFriends.size()==0){
            firstShow.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            firstShow.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        // Setup layout manager for mBlogList and column count
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // Control orientation of the mBlogList
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        // Attach layout manager
        mRecyclerView.setLayoutManager(layoutManager);

        SecretChatRecyclerAdapter adapter = new SecretChatRecyclerAdapter(mFriends);
        mRecyclerView.setAdapter(adapter);

        // Listen to the item touching
        mRecyclerView
                .addOnItemTouchListener(new RecyclerItemClickListenerInFragment(getActivity(), mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent in = new Intent(getActivity(), Chat.class);
                        in.putExtra("Friend_name", mFriends.get(position).getFriName());
                        startActivity(in);
                        Log.d("friend", mFriends.get(position).getFriName());
                    }
                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));


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
                Intent in2 = new Intent(getActivity(), GroupChat.class);
                startActivity(in2);
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

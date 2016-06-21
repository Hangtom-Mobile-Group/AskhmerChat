package com.askhmer.chat.util;

import android.widget.Filter;

import com.askhmer.chat.adapter.FriendAdapter;
import com.askhmer.chat.adapter.SecretChatRecyclerAdapter;
import com.askhmer.chat.model.Friends;

import java.util.ArrayList;

/**
 * Created by DELL E6420 on 6/15/2016.
 */
public class CustomFilterFriend  extends Filter {

    SecretChatRecyclerAdapter adapter;
    ArrayList<Friends> filterList;

    public CustomFilterFriend(ArrayList<Friends> filterList,SecretChatRecyclerAdapter adapter) {
        this.adapter=adapter;
        this.filterList=filterList;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Friends> filteredFriend=new ArrayList<>();

            for (int i=0;i<filterList.size();i++) {
                //CHECK
                if(filterList.get(i).getFriName().toUpperCase().contains(constraint)) {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredFriend.add(filterList.get(i));
                }
            }

            results.count=filteredFriend.size();
            results.values=filteredFriend;
        }else {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.friendList = (ArrayList<Friends>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}

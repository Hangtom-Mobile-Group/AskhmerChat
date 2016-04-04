package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.model.Friends;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {

    private List<Friends> addfriendList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            id = (TextView) view.findViewById(R.id.tv_id);
        }
    }

    public FriendAdapter(List<Friends> addfriendList) {
        this.addfriendList = addfriendList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_list, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Friends addfriend = addfriendList.get(position);
        holder.name.setText(addfriend.getFriName());
        holder.id.setText(addfriend.getChatId());
    }
    @Override
    public int getItemCount() {
        return addfriendList.size();
    }
}

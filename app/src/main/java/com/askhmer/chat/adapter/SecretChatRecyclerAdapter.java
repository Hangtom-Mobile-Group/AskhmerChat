package com.askhmer.chat.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.model.Contact;
import com.askhmer.chat.model.Friends;
import com.askhmer.chat.network.API;
import com.askhmer.chat.util.CustomFilterContact;
import com.askhmer.chat.util.CustomFilterFriend;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longdy on 3/26/2016.
 */
public class SecretChatRecyclerAdapter extends RecyclerView.Adapter<SecretChatRecyclerAdapter.SimpleItemViewHolder>{

    public List<Friends> friendList;

    // Provide a reference to the views for each data item
    // Provide access to all the views for a data item in a view holder
    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        ImageView profileImg;
        TextView name, chatId;
        View isOnline;

        public SimpleItemViewHolder(View itemView) {
            super(itemView);
            profileImg = (ImageView) itemView.findViewById(R.id.layout_round);
            name = (TextView) itemView.findViewById(R.id.tv_friend_name);
            chatId = (TextView) itemView.findViewById(R.id.tv_friend_chat_id);
            isOnline = (View) itemView.findViewById(R.id.v_is_online);
        }


        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public SecretChatRecyclerAdapter(List<Friends> friendList) {
        this.friendList = friendList;
    }

    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.secret_chat_item, viewGroup, false);
        return new SimpleItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleItemViewHolder viewHolder, int position) {

       // viewHolder.profileImg.setImageResource(Integer.parseInt(items.get(position).getImg()));
        String imgPath  = API.UPLOADFILE +friendList.get(position).getImg();
        Picasso.with(viewHolder.profileImg.getContext()).load(imgPath).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(viewHolder.profileImg);

        viewHolder.name.setText(friendList.get(position).getFriName());
       // viewHolder.chatId.setText(items.get(position).getChatId());
        if(friendList.get(position).isOnline()==true){
            viewHolder.isOnline.setVisibility(View.VISIBLE);
        }else {
            viewHolder.isOnline.setVisibility(View.GONE);
        }


    }
    @Override
    public int getItemCount() {
        return this.friendList.size();
    }

    public void clearData() {
        this.friendList.clear();
        this.notifyDataSetChanged();
    }





}

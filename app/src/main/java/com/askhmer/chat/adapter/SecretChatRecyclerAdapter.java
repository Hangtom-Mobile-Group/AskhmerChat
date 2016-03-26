package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.model.Friends;

import java.util.List;

/**
 * Created by Longdy on 3/26/2016.
 */
public class SecretChatRecyclerAdapter extends RecyclerView.Adapter<SecretChatRecyclerAdapter.SimpleItemViewHolder> {

    private List<Friends> items;
    // Provide a reference to the views for each data item
    // Provide access to all the views for a data item in a view holder
    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImg;
        TextView name,chatId;

        public SimpleItemViewHolder(View itemView) {
            super(itemView);
            profileImg = (ImageView) itemView.findViewById(R.id.img_friend_profile);
            name = (TextView) itemView.findViewById(R.id.tv_friend_name);
            chatId = (TextView) itemView.findViewById(R.id.tv_friend_chat_id);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public SecretChatRecyclerAdapter(List<Friends> items) {
        this.items = items;
    }

    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.secret_chat_item, viewGroup, false);
        return new SimpleItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleItemViewHolder viewHolder, int position) {

        viewHolder.profileImg.setImageResource(items.get(position).getImg());
        viewHolder.name.setText(items.get(position).getFriName());
        viewHolder.chatId.setText(items.get(position).getChatId());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }



}

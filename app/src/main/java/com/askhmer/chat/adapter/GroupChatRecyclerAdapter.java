package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.model.Friends;
import com.askhmer.chat.network.API;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Longdy on 3/26/2016.
 */
public class GroupChatRecyclerAdapter extends RecyclerView.Adapter<GroupChatRecyclerAdapter.ViewHolder>{

    private List<Friends> mFriend;

    public GroupChatRecyclerAdapter(List<Friends> mFriend) {
        this.mFriend = mFriend;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvChatId;
        public CheckBox chkSelected;
        public ImageView img;

        public Friends singleFriend;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.tv_friend_name);
            tvChatId = (TextView) itemLayoutView.findViewById(R.id.tv_friend_chat_id);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);
            img = (ImageView) itemLayoutView.findViewById(R.id.layout_round);
        }


    }

    @Override
    public GroupChatRecyclerAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // create a new view
        final View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.group_chat_item, parent,false);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupChatRecyclerAdapter.ViewHolder holder, int position) {

        final int pos = position;

      //  holder.img.setImageResource(Integer.parseInt(mFriend.get(pos).getImg()));
       // String imgPath  = API.UPLOADFILE + mFriend.get(pos).getImg();

        String imgPath=mFriend.get(pos).getImg();;
        boolean found = imgPath.contains("facebook");
        Log.d("found", "Return : " + found);
        String imgPaht1 = API.UPLOADFILE + imgPath;
        String imgPaht2 = imgPath;
        if( found == false){
            Picasso.with(holder.img.getContext()).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.img);
        }else{
            Picasso.with(holder.img.getContext()).load(imgPaht2).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.img);
        }
       // Picasso.with(holder.img.getContext()).load(imgPath).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.img);
        holder.tvName.setText(mFriend.get(pos).getFriName());
        holder.tvChatId.setText(mFriend.get(pos).getChatId());
        holder.chkSelected.setChecked(mFriend.get(pos).isSelected());
        holder.chkSelected.setTag(mFriend.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Friends contact = (Friends) cb.getTag();

                contact.setIsSelected(cb.isChecked());
                mFriend.get(pos).setIsSelected(cb.isChecked());
//                Toast.makeText(
//                        v.getContext(),
//                        "Clicked on Checkbox: " + cb.getText() + " is "
//                                + cb.isChecked(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFriend.size();
    }

    // method to access in activity after updating selection
    public List<Friends> getmFriendtist() {
        return mFriend;
    }

    public void clearData() {
        this.mFriend.clear();
        this.notifyDataSetChanged();
    }
}

package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.model.ChatRoom;
import com.askhmer.chat.network.API;
import com.askhmer.chat.util.MessageConvertor;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Longdy on 3/26/2016.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.SimpleItemViewHolder>{

    public List<ChatRoom> friendList;

    // Provide a reference to the views for each data item
    // Provide access to all the views for a data item in a view holder
    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private ImageView profileImg;
        private TextView name, chatId;
        private View isOnline;
        private TextView numberOfMsgNotSeen;
        private TextView currentMsg;
        private TextView msgDate;
        private RelativeLayout msgNotSeen;

        public SimpleItemViewHolder(View itemView) {
            super(itemView);
            profileImg = (ImageView) itemView.findViewById(R.id.layout_round);
            name = (TextView) itemView.findViewById(R.id.tv_friend_name);
            chatId = (TextView) itemView.findViewById(R.id.tv_friend_chat_id);
            numberOfMsgNotSeen = (TextView) itemView.findViewById(R.id.tv_msg_not_seen);
            currentMsg = (TextView) itemView.findViewById(R.id.tv_current_msg);
            msgDate = (TextView) itemView.findViewById(R.id.tv_msg_date);

            msgNotSeen = (RelativeLayout) itemView.findViewById(R.id.layout_count_current_msg);

            isOnline = (View) itemView.findViewById(R.id.v_is_online);
        }


        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatRoomAdapter(List<ChatRoom> friendList) {
        this.friendList = friendList;
    }

    @Override
    public SimpleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.chat_room_item, viewGroup, false);
        return new SimpleItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleItemViewHolder viewHolder, int position) {

       // viewHolder.profileImg.setImageResource(Integer.parseInt(items.get(position).getImg()));
       // String imgPath  = API.UPLOADFILE +friendList.get(position).getImg();

        String imgPath=friendList.get(position).getImgUrl();
        boolean found = imgPath.contains("facebook");
        Log.d("found", "Return : " + found);
        String imgPaht1 = API.UPLOADFILE + imgPath;
        String imgPaht2 = imgPath;
        if( found == false){
            Picasso.with(viewHolder.profileImg.getContext()).load(imgPaht1).placeholder(R.drawable.groupchat).error(R.drawable.groupchat).into(viewHolder.profileImg);
        }else{
            Picasso.with(viewHolder.profileImg.getContext()).load(imgPaht2).placeholder(R.drawable.groupchat).error(R.drawable.groupchat).into(viewHolder.profileImg);
        }
        viewHolder.name.setText(friendList.get(position).getRoomName());
        viewHolder.currentMsg.setText(MessageConvertor.emojisDecode(friendList.get(position).getCurrentMsg()));
        viewHolder.msgDate.setText(friendList.get(position).getMsgDate());
        viewHolder.numberOfMsgNotSeen.setText(friendList.get(position).getCounterMsgNotSeen()+"");

        if(friendList.get(position).getCounterMsgNotSeen()==0){
            viewHolder.msgNotSeen.setVisibility(View.INVISIBLE);
        }else viewHolder.msgNotSeen.setVisibility(View.VISIBLE);


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

    public void removeData() {
        /*for (int i = 0; i < friendList.size(); i++ ) {
            friendList.remove(i);
            notifyItemRemoved(i);
            notifyItemRangeChanged(i, friendList.size());
        }*/
        while (friendList.size() !=0){
            friendList.remove(0);
            notifyItemRemoved(0);
            notifyItemRangeChanged(0, friendList.size());
        }
    }

    public void changeData(ChatRoom chatRoom) {
        try{
            for (int i = 0; i < friendList.size(); i++) {
                if (friendList.get(i).getRoomId() == chatRoom.getRoomId()) {
                    friendList.set(i, chatRoom);
                    Collections.swap(friendList,i,0);
                    for (int j = 1; i > j; j++) {
                        Collections.swap(friendList,i,j);
                    }
                    break;
                }else if (friendList.size() - 1 == i ) {
                    friendList.add(chatRoom);
                }
            }
            if (friendList.size() == 0) {
                friendList.add(chatRoom);
            }
        }catch (Exception e){

        }
    }
}

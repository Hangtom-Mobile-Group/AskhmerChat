package com.askhmer.chat.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.Chat;
import com.askhmer.chat.activity.UserProfile;
import com.askhmer.chat.model.Friends;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {

    private List<Friends> addfriendList;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, id;
        public Button chat;
        private Context context;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            id = (TextView) view.findViewById(R.id.tv_id);
            chat = (Button) view.findViewById(R.id.btn_chat);

            view.setOnClickListener(this);
            chat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            if(v.getId() == chat.getId()){
                Toast.makeText(v.getContext(), "btn  chat clicked "+addfriendList.get(pos).getFriName(), Toast.LENGTH_SHORT).show();
            }else {
                Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.alert_dialog_profile);

                TextView userNmae = (TextView) dialog.findViewById(R.id.user_name);
                userNmae.setText(addfriendList.get(pos).getFriName());

                dialog.findViewById(R.id.image_bttn_profile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), UserProfile.class);
                        context.startActivity(in);
                    }
                });

                dialog.findViewById(R.id.image_btn_chat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), Chat.class);
                        context.startActivity(in);
                    }
                });
/*
             WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = 610;
                            lp.height = 1000;
                            lp.gravity = Gravity.CENTER;
                            dialog.getWindow().setAttributes(lp);
*/
                dialog.show();
                Toast.makeText(v.getContext(), "Row  chat clicked", Toast.LENGTH_SHORT).show();
            }
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
        final Friends addfriend = addfriendList.get(position);
        holder.name.setText(addfriend.getFriName());
        holder.id.setText(addfriend.getChatId());

/*        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), Chat.class);
                in.putExtra("Friend_name", addfriend.getFriName());
                context.startActivity(in);
            }
        });*/
    }
    @Override
    public int getItemCount() {
        return addfriendList.size();
    }
}

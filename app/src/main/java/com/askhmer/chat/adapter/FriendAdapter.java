package com.askhmer.chat.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.adapters.AbsListViewBindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.activity.Chat;
import com.askhmer.chat.activity.FriendProfile;
import com.askhmer.chat.model.Friends;
import com.askhmer.chat.network.API;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {

    private List<Friends> addfriendList;
    private String imgPath;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, id;
        public ImageButton chat;
        public ImageView imageProfile;
        public LinearLayout row;
        private Context context ;
        private int friid;



        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            id = (TextView) view.findViewById(R.id.tv_id);
            chat = (ImageButton) view.findViewById(R.id.btn_chat);
            row = (LinearLayout) view.findViewById(R.id.list_row);
            imageProfile = (ImageView) view.findViewById(R.id.layout_round);

            view.setOnClickListener(this);
            chat.setOnClickListener(this);
//            row.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            if(v.getId() == chat.getId()){
                Intent in = new Intent(v.getContext(), Chat.class);
                in.putExtra("Friend_name",addfriendList.get(pos).getFriName());
                in.putExtra("friid",addfriendList.get(pos).getFriId());
                v.getContext().startActivity(in);
            }else {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.alert_dialog_profile);

                TextView userNmae = (TextView) dialog.findViewById(R.id.user_name);
                userNmae.setText(addfriendList.get(pos).getFriName());
                ImageView profileImg = (ImageView) dialog.findViewById(R.id.profile_image);
                //String imagePath  ="http://10.0.3.2:8080/ChatAskhmer/resources/upload/file/"+ addfriendList.get(pos).getImg();
                String imagePath  = API.UPLOADFILE + addfriendList.get(pos).getImg();
                Picasso.with(profileImg.getContext()).load(imagePath).into(profileImg);
                friid = addfriendList.get(pos).getFriId();


                dialog.findViewById(R.id.image_bttn_profile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), FriendProfile.class);
                        in.putExtra("friid",friid);
                        v.getContext().startActivity(in);
                        dialog.dismiss();
                    }
                });
/*
                dialog.findViewById(R.id.image_bttn_profile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), UserProfile.class);
                        v.getContext().startActivity(in);
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.image_btn_chat).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), Chat.class);
                        v.getContext().startActivity(in);
                        dialog.dismiss();
                    }
                });
*/
/*
             WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = 610;
                            lp.height = 1000;
                            lp.gravity = Gravity.CENTER;
                            dialog.getWindow().setAttributes(lp);
*/

                dialog.show();

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

        String word = "facebook";
        String text = addfriend.getImg();
        Boolean found;
        found = text.contains(word);
        if(found == true){
            imgPath = addfriend.getImg();
        }else{
           // imgPath  = API.UPLOADFILE +addfriend.getImg();
        }

        Picasso.with(holder.imageProfile.getContext()).load(imgPath).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.imageProfile);

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

    public void clearData() {
        this.addfriendList.clear();
        this.notifyDataSetChanged();
    }

}

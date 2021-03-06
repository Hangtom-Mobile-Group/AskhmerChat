package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.model.Friends;

import java.util.List;

public class AddfriendAdapter extends RecyclerView.Adapter<AddfriendAdapter.MyViewHolder> {

    private List<Friends> addfriendList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id;
        public ImageButton btnAddFri;

        public MyViewHolder(View view) {
            super(view);
            btnAddFri = (ImageButton) view.findViewById(R.id.btn_add_fri);
            name = (TextView) view.findViewById(R.id.tv_name);
            id = (TextView) view.findViewById(R.id.tv_id);

            btnAddFri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if(v.getId() == btnAddFri.getId()) {
                        Toast.makeText(v.getContext(), addfriendList.get(pos).getFriName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public AddfriendAdapter(List<Friends> addfriendList) {
        this.addfriendList = addfriendList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addfriend_list, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Friends addfriend = addfriendList.get(position);

        String subFriName = addfriend.getFriName();
        if(subFriName.length() > 20) {
            subFriName = addfriend.getFriName().substring(0,20) + "...";
        }
        holder.name.setText(subFriName);
        holder.id.setText(addfriend.getChatId());
    }
    @Override
    public int getItemCount() {
        return addfriendList.size();
    }
}

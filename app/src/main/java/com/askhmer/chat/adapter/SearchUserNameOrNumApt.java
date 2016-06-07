package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by soklundy on 5/28/2016.
 */
public class SearchUserNameOrNumApt extends RecyclerView.Adapter<SearchUserNameOrNumApt.MyViewHolder> {

    private List<User> users;

    public SearchUserNameOrNumApt(List<User> users){
        this.users = users;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.search_id_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.id.setText(users.get(position).getUserId());
        holder.name.setText(users.get(position).getUserName());
        holder.gender.setText(users.get(position).getGender());

        holder.addMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Add", Toast.LENGTH_SHORT).show();
            }
        });

        // Using picasso
        if (users.get(position).getUserPhoto() == null || users.get(position).getUserPhoto().isEmpty()) {
            Picasso.with(holder.image.getContext()).load(R.drawable.profile)
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(holder.image);
        }else {
            Picasso.with(holder.image.getContext()).load(users.get(position).getUserPhoto())
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     *
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id, gender;
        public CircleImageView image;
        public Button addMe;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView)view.findViewById(R.id.user_id);
            name = (TextView)view.findViewById(R.id.tv_name);
            gender = (TextView)view.findViewById(R.id.gender);
            image = (CircleImageView)view.findViewById(R.id.layout_round);
            addMe = (Button)view.findViewById(R.id.add_friend);
        }
    }

}

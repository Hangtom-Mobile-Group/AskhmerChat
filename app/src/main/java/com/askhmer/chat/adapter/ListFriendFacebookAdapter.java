package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.askhmer.chat.R;
import com.askhmer.chat.model.DataFriends;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Thoeurn on 4/29/2016.
 */
public class ListFriendFacebookAdapter extends RecyclerView.Adapter<ListFriendFacebookAdapter.MyViewHolder>{

        private ArrayList<DataFriends> lstfriends;

        public ListFriendFacebookAdapter(ArrayList<DataFriends> lstfriends){
            this.lstfriends = lstfriends;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_facebook_friends, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            DataFriends fri = lstfriends.get(position);
            holder.id.setText(fri.getId());
            holder.name.setText(fri.getName());
            URL theUrl = null;

            try {
                theUrl = new URL("https://graph.facebook.com/"+fri.getId()+"/picture?width=400&height=400");
                Log.i("onUrl => ", theUrl.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // Using picasso
            Picasso.with(holder.fbImage.getContext()).load(theUrl.toString())
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(holder.fbImage);

            holder.addFriendFB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(position);
                    Toast.makeText(v.getContext(), "Hits" + holder.id.getText().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return lstfriends.size();
        }

        public void removeAt(int position) {
            lstfriends.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, lstfriends.size());
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView id, name;
            public CircleImageView fbImage;
            public Button addFriendFB;

            public MyViewHolder(View view){
                super(view);
                id = (TextView)view.findViewById(R.id.tv_id);
                name = (TextView)view.findViewById(R.id.tv_name);
                fbImage = (CircleImageView)view.findViewById(R.id.imgfbfriend);
                addFriendFB = (Button)view.findViewById(R.id.addfriendfb);
            }
        }
}

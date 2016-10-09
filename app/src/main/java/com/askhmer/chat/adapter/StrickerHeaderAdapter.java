package com.askhmer.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.model.HeaderStricker;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by soklundy on 10/8/2016.
 */

public class StrickerHeaderAdapter extends RecyclerView.Adapter<StrickerHeaderAdapter.MyViewHolder>{


    private List<HeaderStricker> headerStrickers;
    private SharedPreferencesFile sharedPreferencesFile;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.header_icon);
            linearLayout = (LinearLayout) view.findViewById(R.id.item_row);
        }
    }

    public StrickerHeaderAdapter(List<HeaderStricker> headerStrickers, Context context) {
        this.headerStrickers = headerStrickers;
        sharedPreferencesFile = SharedPreferencesFile.newInstance(context, "person");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.header_stricker_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HeaderStricker movie = headerStrickers.get(position);
        Picasso.with(holder.imageView.getContext()).load("http://chat.askhmer.com/resources/upload/file/sticker/" + headerStrickers.get(position).getAlbumImage()).into(holder.imageView);
        if (headerStrickers.get(position).isSelected()) {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#d5d5d5"));
        } else {
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return headerStrickers.size();
    }

    public void setSelected(int pos) {
        try {
            if (headerStrickers.size() > 1) {
                headerStrickers.get(sharedPreferencesFile.getIntSharedPreference("person", "person")).setSelected(false);
                sharedPreferencesFile.putIntSharedPreference("person", "person", pos);
            }
            headerStrickers.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

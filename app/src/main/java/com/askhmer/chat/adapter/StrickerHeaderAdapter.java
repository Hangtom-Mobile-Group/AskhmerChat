package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.model.HeaderStricker;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by soklundy on 10/8/2016.
 */

public class StrickerHeaderAdapter extends RecyclerView.Adapter<StrickerHeaderAdapter.MyViewHolder>{


    private List<HeaderStricker> headerStrickers;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.header_icon);
        }
    }

    public StrickerHeaderAdapter(List<HeaderStricker> headerStrickers) {
        this.headerStrickers = headerStrickers;
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
    }

    @Override
    public int getItemCount() {
        return headerStrickers.size();
    }
}

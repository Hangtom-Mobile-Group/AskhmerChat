package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.askhmer.chat.R;
import com.askhmer.chat.model.BodyStricker;
import com.askhmer.chat.model.HeaderStricker;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by soklundy on 10/8/2016.
 */

public class StrickerBodyAdapter extends RecyclerView.Adapter<StrickerBodyAdapter.MyViewHolder>{


    private List<BodyStricker> bodyStrickers;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.header_icon);
        }
    }

    public StrickerBodyAdapter(List<BodyStricker> bodyStrickers) {
        this.bodyStrickers = bodyStrickers;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.body_stricker_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BodyStricker bodyStricker = bodyStrickers.get(position);
        Picasso.with(holder.imageView.getContext()).load("http://chat.askhmer.com/resources/upload/file/sticker/" + bodyStrickers.get(position).getAlbumImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return bodyStrickers.size();
    }
}

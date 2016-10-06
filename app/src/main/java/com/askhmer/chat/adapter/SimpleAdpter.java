package com.askhmer.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.askhmer.chat.R;


/**
 * Created by soklundy on 8/23/2016.
 */
public class SimpleAdpter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private boolean isGrid;

    public SimpleAdpter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {

            view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        switch (position) {
            case 0:
                viewHolder.textView.setText(context.getString(R.string.facebook));
                viewHolder.imageView.setImageResource(R.drawable.facebook);
                break;
            case 1:
                viewHolder.textView.setText(context.getString(R.string.fb_messager));
                viewHolder.imageView.setImageResource(R.drawable.fb_messager);
                break;
            case 2:
                viewHolder.textView.setText(context.getString(R.string.ko_line));
                viewHolder.imageView.setImageResource(R.drawable.ko_line);
                break;
            default:
                viewHolder.textView.setText(context.getString(R.string.whatsapp));
                viewHolder.imageView.setImageResource(R.drawable.whatsapp);
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}

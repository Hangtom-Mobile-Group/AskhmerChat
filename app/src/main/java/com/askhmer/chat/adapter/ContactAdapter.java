package com.askhmer.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.askhmer.chat.R;
import com.askhmer.chat.model.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private List<Contact> contactList;
    // generate the random integers for r, g and b value




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id, listName;
        public View bg_list_name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvname);
            id = (TextView) view.findViewById(R.id.tv_phone_number);
            listName = (TextView) view.findViewById(R.id.tv_contact_list_name);
            bg_list_name = view.findViewById(R.id.vi_bg_contact_name);

        }
    }

    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        holder.name.setText(contact.getName());
        holder.id.setText(contact.getPhoneNumber());
        holder.listName.setText(contact.getSubName());
        holder.bg_list_name.setBackgroundColor(contact.getBgColor());
    }
    @Override
    public int getItemCount() {
        return contactList.size();
    }
}

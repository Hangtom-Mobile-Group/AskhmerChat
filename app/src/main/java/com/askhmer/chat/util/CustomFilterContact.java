package com.askhmer.chat.util;

import android.widget.Filter;

import com.askhmer.chat.adapter.ContactAdapter;
import com.askhmer.chat.model.Contact;

import java.util.ArrayList;

/**
 * Created by soklundy on 4/19/2016.
 */
public class CustomFilterContact extends Filter {

    ContactAdapter adapter;
    ArrayList<Contact> filterList;

    public CustomFilterContact(ArrayList<Contact> filterList,ContactAdapter adapter) {
        this.adapter=adapter;
        this.filterList=filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Contact> filteredContact=new ArrayList<>();

            for (int i=0;i<filterList.size();i++) {
                //CHECK
                if(filterList.get(i).getName().toUpperCase().contains(constraint)) {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredContact.add(filterList.get(i));
                }
            }

            results.count=filteredContact.size();
            results.values=filteredContact;
        }else {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.contactList = (ArrayList<Contact>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}

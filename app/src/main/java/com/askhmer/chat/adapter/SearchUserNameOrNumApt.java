package com.askhmer.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.model.DataFriends;
import com.askhmer.chat.model.User;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by soklundy on 5/28/2016.
 */
public class SearchUserNameOrNumApt extends RecyclerView.Adapter<SearchUserNameOrNumApt.MyViewHolder> {

    private ArrayList<User> users;
    String user_id;
    String friend_id;
    private Context context;
    private SharedPreferencesFile mSharedPrefer;

    public SearchUserNameOrNumApt(ArrayList<User> users){
        this.users = users;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.search_id_item, parent, false);

        mSharedPrefer = SharedPreferencesFile.newInstance(itemView.getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        friend_id = users.get(position).getUserId();
        holder.id.setText(users.get(position).getUserId());
        holder.name.setText(users.get(position).getUserName());
        holder.gender.setText(users.get(position).getGender());


        holder.addMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                addFriend();
                removeAt(position);
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
            String path=users.get(position).getUserPhoto();
            boolean found = path.contains("facebook");
            Log.d("found", "Return : " + found);
            String imgPaht1 = API.UPLOADFILE +path;
            String imgPaht2 = path;
            if( found == false){
                Picasso.with(holder.image.getContext()).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.image);
            }else{
                Picasso.with(holder.image.getContext()).load(imgPaht2).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.image);
            }

//            Picasso.with(holder.image.getContext()).load(path)
//                    .fit()
//                    .centerCrop()
//                    .noFade()
//                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public void removeAt(int position) {
        users.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, users.size());
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




    public void addFriend(){
        JSONObject params;
        try {
            params = new JSONObject();
            params.put("friendId",friend_id);
            params.put("userId",user_id);
            String url = "http://chat.askhmer.com/api/friend/add";

            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("STATUS").equals("200")) {
                            Log.d("love", response.toString());
                            //removeAt(position);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "Unsuccessfully Added !!", Toast.LENGTH_LONG).show();

                    } finally {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // Toast.makeText(getBaseContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(context).addToRequestQueue(jsonRequest);
        } catch (JSONException e) {
            Toast.makeText(context, "ERROR_MESSAGE_JSONOBJECT" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

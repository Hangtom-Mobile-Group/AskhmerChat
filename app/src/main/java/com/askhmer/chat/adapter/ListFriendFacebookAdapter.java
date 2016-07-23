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
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Thoeurn on 4/29/2016.
 */
public class ListFriendFacebookAdapter extends RecyclerView.Adapter<ListFriendFacebookAdapter.MyViewHolder>{

        private ArrayList<DataFriends> lstfriends;
        private Context context;
        private String facebook_id;
        private int  friend_id;
        private String user_id;
        private SharedPreferencesFile mSharedPrefer;

        public ListFriendFacebookAdapter(ArrayList<DataFriends> lstfriends){
            this.lstfriends = lstfriends;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_facebook_friends, parent, false);

            mSharedPrefer = SharedPreferencesFile.newInstance(itemView.getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
            user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final DataFriends fri = lstfriends.get(position);
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
                public void onClick(final View v) {
                    // removeAt(position);
                    //   Toast.makeText(v.getContext(), "Hits" + holder.id.getText().toString(), Toast.LENGTH_LONG).show();
                    facebook_id = holder.id.getText().toString();
                    String url = "http://chat.askhmer.com/api/user/getUserIdByFacebookId/" + facebook_id;
                    GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("STATUS")) {
                                    JSONObject object = response.getJSONObject("DATA");
                                    friend_id = object.getInt("userId");
                                    Log.d("google", "id :" + friend_id);
//                        Toast.makeText(context, "friend id :"+ friend_id, Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("google", "Invalid User Id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                //Toast.makeText(v.getContext(),user_id +"  "+friend_id, Toast.LENGTH_SHORT).show();
                                addFriend();
                                removeAt(position);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("google", "There is Something Wrong !!");
                            Log.d("error", error.toString());
                        }
                    });
                    // Add request queue
                    MySingleton.getInstance(context).addToRequestQueue(jsonRequest);
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
          //  notifyItemRangeChanged(position, lstfriends.size());
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



    public void addFriend() {
        JSONObject params;
        try {
            params = new JSONObject();
            params.put("friendId", friend_id);
            params.put("userId", user_id);
           // String url = "http://chat.askhmer.com/api/friend/add";
            String url ="http://chat.askhmer.com/api/friend/add";

            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("STATUS").equals("200")) {
                            Log.d("love", response.toString());
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

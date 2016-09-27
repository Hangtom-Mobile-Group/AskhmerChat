package com.askhmer.chat.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.activity.Chat;
import com.askhmer.chat.activity.FriendProfile;
import com.askhmer.chat.model.Friends;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Torn Longdy on 14/09/16.
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<ExpandableListAdapter.MyViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Friends> data;
    private String imgPath;
    String user_id;
    private SharedPreferencesFile mSharedPrefer;

    public ExpandableListAdapter(List<Friends> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        View itemView = null;

        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_header, parent, false);
                MyViewHolder header = new MyViewHolder(view,"");
                return header;
            case CHILD:
                LayoutInflater inflaterChild = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = inflaterChild.inflate(R.layout.friends_list, parent, false);

                return new MyViewHolder(itemView);
        }
        return null;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
//        final Item item = data.get(position);

        final Friends addfriend = data.get(position);

        switch (addfriend.type) {
            case HEADER:
                final MyViewHolder itemController = (MyViewHolder) holder;
                itemController.refferalItem = addfriend;
                itemController.header_title.setText(addfriend.header);
                if (addfriend.invisibleChildren == null) {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                    Log.e("test","invisibleChildren == null");
                } else {

                    itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                    Log.e("test", "invisibleChildren != null");
                }
                itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addfriend.invisibleChildren == null) {
                            addfriend.invisibleChildren = new ArrayList<Friends>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                addfriend.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                            Log.e("test", "onclick invisibleChildren == null");
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Friends i : addfriend.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                            addfriend.invisibleChildren = null;
                            Log.e("test", "onclick invisibleChildren != null");
                        }
                    }
                });
//                itemController.btn_expand_toggle.callOnClick();
                break;
            case CHILD:

                holder.name.setText(addfriend.getFriName());
                holder.id.setText(addfriend.getChatId());
                if(addfriend.isFriend()){
                    holder.confirm.setVisibility(View.GONE);
                    holder.chat.setVisibility(View.VISIBLE);
                }else{
                    holder.chat.setVisibility(View.GONE);
                    holder.confirm.setVisibility(View.VISIBLE);
                }


                String str=addfriend.getImg();
                boolean found = str.contains("facebook");
                Log.d("found", "Return : " + found);
                String imgPaht1 = API.UPLOADFILE +addfriend.getImg();
                String imgPaht2 = addfriend.getImg();
                if( found == false){
                    Picasso.with(holder.imageProfile.getContext()).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.imageProfile);
                }else{
                    Picasso.with(holder.imageProfile.getContext()).load(imgPaht2).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.imageProfile);
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, id;
        public ImageButton chat, confirm;
        public ImageView imageProfile;
        public LinearLayout row;
        private Context context ;
        private int friid;
        private int groupID = 0;

        public TextView header_title;
        public ImageView btn_expand_toggle;
        public Friends refferalItem;

        public MyViewHolder(View itemView,String str) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
        }

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            id = (TextView) view.findViewById(R.id.tv_id);
            chat = (ImageButton) view.findViewById(R.id.btn_chat);
            row = (LinearLayout) view.findViewById(R.id.list_row);
            imageProfile = (ImageView) view.findViewById(R.id.layout_round);
            confirm = (ImageButton) view.findViewById(R.id.btn_confirm);

            view.setOnClickListener(this);
            chat.setOnClickListener(this);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int pos = getAdapterPosition();
                    mSharedPrefer = SharedPreferencesFile.newInstance(v.getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
                    user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);

//                    Toast.makeText(v.getContext(), addfriendList.get(pos).getFriId() + " " + user_id, Toast.LENGTH_SHORT).show();

                    String url = API.CONFIRM + data.get(pos).getFriId() + "/" + user_id;
                    GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.PUT, url, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("STATUS")) {
                                    Log.d("confirm", response.toString());
                                    Toast.makeText(v.getContext(), "Confirm Succesful !!", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(v.getContext(), "Unsuccessfully Confirm !!", Toast.LENGTH_LONG).show();
                            } finally {
                                confirm.setVisibility(View.GONE);
                                chat.setVisibility(View.VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(v.getContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    MySingleton.getInstance(v.getContext()).addToRequestQueue(jsonRequest);


                }
            });
        }

        @Override
        public void onClick(final View v) {
            final int pos = getAdapterPosition();

            if(v.getId() == chat.getId()){

                mSharedPrefer = SharedPreferencesFile.newInstance(v.getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
                user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
                String friID = String.valueOf(data.get(pos).getFriId());
                Log.e("friend id",friID);

                String url = API.CHECKCHATROOM+ user_id + "/"+ friID;
                GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            try {
                                if (response.getInt("STATUS") == 200) {
                                    groupID = response.getInt("MESSAGE_ROOM_ID");
                                    Log.e("group id", groupID + "");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } finally {
                            Intent in = new Intent(v.getContext(), Chat.class);
                            in.putExtra("Friend_name",data.get(pos).getFriName());
                            in.putExtra("friid",data.get(pos).getFriId());
                            in.putExtra("groupID",groupID);
                            v.getContext().startActivity(in);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(context,"error",Toast.LENGTH_LONG).show();
                        Log.e("error","error");
                    }
                });
                MySingleton.getInstance(v.getContext()).addToRequestQueue(objectRequest);


            } else {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.alert_dialog_profile);

                final TextView userNmae = (TextView) dialog.findViewById(R.id.user_name);
                userNmae.setText(data.get(pos).getFriName());
                ImageView profileImg = (ImageView) dialog.findViewById(R.id.profile_image);
                String str = data.get(pos).getImg();
                boolean found = str.contains("facebook");
                Log.d("found", "Return : " + found);
                String imgPaht1 = API.UPLOADFILE + str;
                String imgPaht2 = str;
                if (found == false) {
                    Picasso.with(profileImg.getContext()).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(profileImg);
                } else {
                    Picasso.with(profileImg.getContext()).load(imgPaht2).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(profileImg);
                }


                // Picasso.with(profileImg.getContext()).load(imagePath).into(profileImg);
                friid = data.get(pos).getFriId();


                dialog.findViewById(R.id.image_bttn_profile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), FriendProfile.class);
                        in.putExtra("friid", friid);
                        v.getContext().startActivity(in);
                        dialog.dismiss();
                    }
                });
/*
                dialog.findViewById(R.id.image_bttn_profile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), UserProfile.class);
                        v.getContext().startActivity(in);
                        dialog.dismiss();
                    }
                });*/
                dialog.findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), FriendProfile.class);
                        in.putExtra("friid", friid);
                        v.getContext().startActivity(in);
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.image_btn_chat).setOnClickListener(new View.OnClickListener() {
                    int pos = getAdapterPosition();

                    @Override
                    public void onClick(final View v) {


                        mSharedPrefer = SharedPreferencesFile.newInstance(v.getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
                        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
                        String friID = String.valueOf(data.get(pos).getFriId());
                        Log.e("friend id",friID);

                        String url = API.CHECKCHATROOM+ user_id + "/"+ friID;
                        GsonObjectRequest objectRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    try {
                                        if (response.getInt("STATUS") == 200) {
                                            groupID = response.getInt("MESSAGE_ROOM_ID");
                                            Log.e("group id", groupID + "");

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } finally {
                                    Intent in = new Intent(v.getContext(), Chat.class);
                                    in.putExtra("Friend_name",data.get(pos).getFriName());
                                    in.putExtra("friid", data.get(pos).getFriId());
                                    in.putExtra("groupID", groupID);
                                    v.getContext().startActivity(in);
                                    dialog.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Toast.makeText(context,"error",Toast.LENGTH_LONG).show();
                                Log.e("error","error");
                            }
                        });
                        MySingleton.getInstance(v.getContext()).addToRequestQueue(objectRequest);

                    }
                });


                /**
                 * block code check friend or not
                 */

                boolean is_friend = data.get(pos).isFriend();

                if(is_friend){
                    dialog.findViewById(R.id.image_btn_delete_friend).setVisibility(View.GONE);
                    dialog.findViewById(R.id.image_btn_unfriend).setVisibility(View.VISIBLE);

                }else{
                    dialog.findViewById(R.id.image_btn_unfriend).setVisibility(View.GONE);
                    dialog.findViewById(R.id.image_btn_delete_friend).setVisibility(View.VISIBLE);

                }

                /* code for delete friend */
                dialog.findViewById(R.id.image_btn_delete_friend).setOnClickListener(new View.OnClickListener() {
                    int pos = getAdapterPosition();

                    @Override
                    public void onClick(View v) {
                        mSharedPrefer = SharedPreferencesFile.newInstance(v.getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
                        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
                        String friend_id = String.valueOf(data.get(pos).getFriId());
                        try {
                            String url = "http://chat.askhmer.com/api/friend/deletefriend/"+friend_id+"/"+user_id ;
                            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.DELETE, url, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("response", response.toString());
                                    try {
                                        if (response.getInt("STATUS")==200) {
                                            Log.d("lov", response.toString());
                                            removeAt(pos);
                                        } else {
                                            Log.e("delete_friend", "hello");
                                        }
                                    } catch (
                                            JSONException e
                                            )

                                    {
                                        Log.d("lov", response.toString());
                                    } finally

                                    {
                                        dialog.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    //Toast.makeText(getBaseContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                                    Log.e("delete", volleyError.toString());
                                }
                            });
                            MySingleton.getInstance(v.getContext()).addToRequestQueue(jsonRequest);
                        } catch (
                                Exception e
                                )

                        {
                            //Toast.makeText(UserProfile.this, "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /* code for unfriend  */
                dialog.findViewById(R.id.image_btn_unfriend).setOnClickListener(new View.OnClickListener() {
                    int pos = getAdapterPosition();
                    @Override
                    public void onClick(View v) {
                        mSharedPrefer = SharedPreferencesFile.newInstance(v.getContext(), SharedPreferencesFile.PREFER_FILE_NAME);
                        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);
                        JSONObject params;
                        try {
                            params = new JSONObject();

                            params.put("id",0);
                            params.put("friendId",data.get(pos).getFriId() );
                            params.put("userId", user_id);
                            params.put("userPhoto", "string");
                            params.put("friend", true);
                            String url = "http://chat.askhmer.com/api/friend/unfriend";
                            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getInt("STATUS")==200) {
                                            Log.d("lov", response.toString());
                                            removeAt(pos);
                                        }
                                    } catch (JSONException e) {
                                        Log.d("lov", response.toString());
                                        // Toast.makeText(v.getContext(), "Unsuccessfully Edited !!", Toast.LENGTH_LONG).show();
                                    } finally {
                                        dialog.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    //  Toast.makeText(getBaseContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            MySingleton.getInstance(v.getContext()).addToRequestQueue(jsonRequest);
                        } catch (JSONException e) {
                            //  Toast.makeText(UserProfile.this, "ERROR_MESSAGE_JSONOBJECT" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            //Toast.makeText(UserProfile.this, "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();

            }
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public void clearData() {
        this.data.clear();
        this.notifyDataSetChanged();
    }




    public void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }




}
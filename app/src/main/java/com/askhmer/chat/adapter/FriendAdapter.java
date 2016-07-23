package com.askhmer.chat.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {

    private List<Friends> addfriendList;
    private String imgPath;
    String user_id;
    private SharedPreferencesFile mSharedPrefer;





    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, id;
        public ImageButton chat, confirm;
        public ImageView imageProfile;
        public LinearLayout row;
        private Context context ;
        private int friid;





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
                    Toast.makeText(v.getContext(), addfriendList.get(pos).getFriId()+"", Toast.LENGTH_SHORT).show();
                    Log.d("XX","xx");
                   String url= API.CONFIRM +user_id+"/"+ addfriendList.get(pos).getFriId();
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
        public void onClick(View v) {
            int pos = getAdapterPosition();

            if(v.getId() == chat.getId()){
                Intent in = new Intent(v.getContext(), Chat.class);
                in.putExtra("Friend_name",addfriendList.get(pos).getFriName());
                in.putExtra("friid",addfriendList.get(pos).getFriId());
                v.getContext().startActivity(in);
            } else {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.alert_dialog_profile);

                final TextView userNmae = (TextView) dialog.findViewById(R.id.user_name);
                userNmae.setText(addfriendList.get(pos).getFriName());
                ImageView profileImg = (ImageView) dialog.findViewById(R.id.profile_image);
                String str = addfriendList.get(pos).getImg();
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
                friid = addfriendList.get(pos).getFriId();


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
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), Chat.class);
                        in.putExtra("friid", friid);
                        v.getContext().startActivity(in);
                        dialog.dismiss();
                    }
                });


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
                            params.put("friendId",addfriendList.get(pos).getFriId() );
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




/*
             WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = 610;
                            lp.height = 1000;
                            lp.gravity = Gravity.CENTER;
                            dialog.getWindow().setAttributes(lp);
*/

                dialog.show();

            }
        }
    }

    public FriendAdapter(List<Friends> addfriendList) {
        this.addfriendList = addfriendList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_list, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Friends addfriend = addfriendList.get(position);
        holder.name.setText(addfriend.getFriName());
        holder.id.setText(addfriend.getChatId());
        String str=addfriend.getImg();
        boolean found = str.contains("facebook");
        Log.d("found","Return : "+ found);
        String imgPaht1 = API.UPLOADFILE +addfriend.getImg();
        String imgPaht2 = addfriend.getImg();

        if(addfriend.isFriend()){
            holder.confirm.setVisibility(View.GONE);
            holder.chat.setVisibility(View.VISIBLE);
        }else{
            holder.chat.setVisibility(View.GONE);
            holder.confirm.setVisibility(View.VISIBLE);
        }


        if( found == false){
            Picasso.with(holder.imageProfile.getContext()).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.imageProfile);
        }else{
            Picasso.with(holder.imageProfile.getContext()).load(imgPaht2).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.imageProfile);
        }

//        imgPath  = API.UPLOADFILE +addfriend.getImg();
//        Picasso.with(holder.imageProfile.getContext()).load(imgPath).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(holder.imageProfile);

/*        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), Chat.class);
                in.putExtra("Friend_name", addfriend.getFriName());
                context.startActivity(in);
            }
        });*/
    }
    @Override
    public int getItemCount() {
        return addfriendList.size();
    }

    public void clearData() {
        this.addfriendList.clear();
        this.notifyDataSetChanged();
    }
}

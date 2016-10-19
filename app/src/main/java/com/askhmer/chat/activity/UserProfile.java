package com.askhmer.chat.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.askhmer.chat.R;
import com.askhmer.chat.SwipeBackLib;
import com.askhmer.chat.network.API;
import com.askhmer.chat.network.GsonObjectRequest;
import com.askhmer.chat.network.MySingleton;
import com.askhmer.chat.util.BitmapEfficient;
import com.askhmer.chat.util.MultipartUtility;
import com.askhmer.chat.util.SharedPreferencesFile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class UserProfile extends SwipeBackLib {

    private ImageView imageProfile;
    private Bitmap bitmap;
    private String imgUrl;
    private String imagePath;
    private String[] uploadImgPath;
    private String picturePath = null;
    private static int RESULT_LOAD_IMAGE_PROFILE = 1;
    private boolean isChangeProfileImage;

    private String imagePathView;
    private EditText editTextId;
    private EditText editTextPhone;
    private EditText editTextMail;
    private EditText editTextCurrentCity;
    private EditText editTextHomeTown;

    private ImageButton editId;
    private ImageButton editPhone;
    private ImageButton editMail;
    private ImageButton editHome;
    private ImageButton editPOB;

    String  user_name;
    String user_id;
    private SharedPreferencesFile mSharedPrefer;

    private SwipeBackLayout mSwipeBackLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        mSharedPrefer = SharedPreferencesFile.newInstance(getApplicationContext(),SharedPreferencesFile.PREFER_FILE_NAME);
        user_id = mSharedPrefer.getStringSharedPreference(SharedPreferencesFile.USERIDKEY);


        requestResponse(user_id);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_name = extras.getString("user_name");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(user_name);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_PROFILE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageProfile = (ImageView)findViewById(R.id.imageView);


        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent in = new Intent(UserProfile.this, ViewPhoto.class);
                    in.putExtra("image", imagePathView);
                    startActivity(in);
                    overridePendingTransition(R.anim.zoom_exit, R.anim.zoom_enter);
            }
        });


        editTextId = (EditText)findViewById(R.id.editText_id);
        editTextPhone = (EditText)findViewById(R.id.editText_phone);
        editTextMail = (EditText)findViewById(R.id.editText_email);
        editTextCurrentCity = (EditText)findViewById(R.id.editText_current_city);
        editTextHomeTown = (EditText)findViewById(R.id.editText_hometown);

        editId = (ImageButton)findViewById(R.id.edit_id);
        editPhone = (ImageButton)findViewById(R.id.edit_phone);
        editMail = (ImageButton)findViewById(R.id.edit_email);
        editHome = (ImageButton)findViewById(R.id.edit_home);
        editPOB = (ImageButton) findViewById(R.id.edit_pob);


        editId.setOnClickListener(editIdClick);
        editPhone.setOnClickListener(editPhoneClick);
        editMail.setOnClickListener(editMailClick);
        editHome.setOnClickListener(editHomeClick);
        editPOB.setOnClickListener(editPOBClick);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.save:
                if (isChangeProfileImage) {
                    new UploadTask().execute(picturePath);
                } else {
                    requestUpdate();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener editIdClick = new View.OnClickListener() {
        public void onClick(View v) {
            //editTextAction(editTextId);
            if(editTextId.getText().toString().equals("")){
                Intent in = new Intent(UserProfile.this, UserId.class);
                startActivity(in);
            }else{
                AlertDialog.Builder msg = new AlertDialog.Builder(UserProfile.this);
                msg.setMessage("Your ID can not be change.");
                msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                msg.show();
            }
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener editPhoneClick = new View.OnClickListener() {
        public void onClick(View v) {
            editTextAction(editTextPhone);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener editMailClick = new View.OnClickListener() {
        public void onClick(View v) {
            editTextAction(editTextMail);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener editHomeClick = new View.OnClickListener() {
        public void onClick(View v) {
            editTextAction(editTextCurrentCity);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener editPOBClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editTextAction(editTextHomeTown);
        }
    };

    private void editTextAction (EditText editText) {
        editText.setEnabled(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void editTextAction (EditText editText, EditText editText1) {
        editText.setEnabled(true);
        editText.requestFocus();

        editText1.setEnabled(true);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    // Load image from server
    public void requestResponse(String user_id) {
      String url = API.VIEWUSERPROFILE + user_id;
        GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("STATUS")) {
                        JSONObject object = response.getJSONObject("DATA");
                        imagePath = object.getString("userPhoto");
                        imagePathView = object.getString("userPhoto");

                        user_name = object.getString("userName");
                        if(!object.getString("userNo").equals("null")){
                            editTextId.setText(object.getString("userNo"));
                        }
                        if(!object.getString("userEmail").equals("null")){
                            editTextMail.setText(object.getString("userEmail"));
                        }

                        if(!object.getString("userPhoneNum").equals("null")){
                            editTextPhone.setText(object.getString("userPhoneNum"));
                        }

                        if(!object.getString("userCurrentCity").equals("null")){
                            editTextCurrentCity.setText(object.getString("userCurrentCity"));
                        }

                        if(!object.getString("userHometown").equals("null")){
                            editTextHomeTown.setText(object.getString("userHometown"));
                        }

                        String str=imagePath;
                        boolean found = str.contains("facebook");
                        Log.d("found","Return : "+ found);

                        String imgPaht1 = API.UPLOADFILE +imagePath;
                        String imgPaht2 = imagePath;
                        if( found == false){
                            imagePathView = imgPaht1;
                            Picasso.with(getApplicationContext()).load(imgPaht1).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(imageProfile);
                            mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.IMGPATH, imgPaht1);
//                            Log.e("img_profile1",imgPaht1);
                        }else{
                            imagePathView = imgPaht2;
                            Picasso.with(getApplicationContext()).load(imgPaht2).placeholder(R.drawable.icon_user).error(R.drawable.icon_user).into(imageProfile);
                            mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.IMGPATH, imgPaht2);
//                            Log.e("img_profile1", imgPaht1);
                        }


                        //final String imgProfile ="http://10.0.3.2:8080/ChatAskhmer/resources/upload/file/"+ imagePath;

//                        final String imgProfile = API.UPLOADFILE+ imagePath;
//                        Log.d("path", imgProfile);
//                        Picasso.with(getApplicationContext())
//                                .load(imgProfile)
//                                .placeholder(R.drawable.icon_user)
//                                .error(R.drawable.icon_user).into(imageProfile);
                    } else {
                        Toast.makeText(UserProfile.this, "Invalid User Id", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserProfile.this, "There is Something Wrong !!", Toast.LENGTH_LONG).show();
            }
        });
        // Add request queue
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE_PROFILE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            //----
            Picasso.with(UserProfile.this).load(selectedImage).into(imageProfile);
            //---

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            if (imageHeight > 400 && imageWidth > 400) {
                bitmap = BitmapEfficient.decodeSampledBitmapFromFile(picturePath, 400, 400);
            } else {
                bitmap = BitmapFactory.decodeFile(picturePath);
            }
          //  imageProfile.setImageBitmap(bitmap);
            isChangeProfileImage = true;
        }
    }

    // upload image process background
   // private class UploadTask extends AsyncTask<String, Void, Void> {
   private class UploadTask extends AsyncTask<String, Void, Void> {

        String url = API.UPLOAD;
        String charset = "UTF-8";
        String responseContent = null;
        File file = null;

        @Override
        protected Void doInBackground(String... params) {
            sendFileToServer(params[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            file = BitmapEfficient.persistImage(bitmap, getApplicationContext());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //  progressSpinning.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
            if (responseContent != null) {
                try {
                    JSONObject object = new JSONObject(responseContent);
                    if (object.getBoolean("STATUS") == true) {
                        imgUrl = object.getString("IMG");
                        uploadImgPath = imgUrl.split("file/");
                        imagePath = uploadImgPath[1];
                        mSharedPrefer.putStringSharedPreference(SharedPreferencesFile.IMGPATH, imagePath);
                        Log.e("img_profile","upload"+imagePath);
                        Toast.makeText(UserProfile.this, "Change Successfully !", Toast.LENGTH_SHORT).show();
                        requestUpdate();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(UserProfile.this, "Uploaded Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        // upload large file size
        public void sendFileToServer(String filePath) {
            try {
            //                MultipartUtility multipart = new MultipartUtility(url, charset);
//                multipart.addFilePart("fileUpload", file);
//                List<String> response = multipart.finish();
//                for (String line : response) {
//                    if (line != null) {
//                        responseContent = line;
//                        break;
//                    }
//                }
                MultipartUtility multipart = new MultipartUtility(url, charset);
                //multipart.addFilePart("fileUpload", file);
                if(file==null){
                    Log.e("filenull","filenull");
                }else{
                    Log.e("filenull","filenotnull");
                    Log.e("filenull",file.getName()+"filenotnull");
                }
                multipart.addFilePart("image", file);
              //  multipart.addFormField("url", "user");
                List<String> response = multipart.finish();
                Log.e("UploadProcess",response.toString());
                for (String line : response) {
                    if (line != null) {
                        responseContent = line;
                        break;
                    }
                }
            } catch (IOException ex) {
                System.err.println(ex);
                Log.e("Error =", ex.toString());
            }
        }

    }

    // update user name
    public void requestUpdate() {
        SharedPreferences session = getBaseContext().getSharedPreferences("userSession", 0);
        // imagePath = (isChangeProfileImage) ? imagePath : session.getString("profile_picture", "N/A");
        imagePath = (isChangeProfileImage) ? imagePath : session.getString("profile_picture", imagePath);
        JSONObject params;
        try {
            params = new JSONObject();
            params.put("userId", user_id);
            params.put("userName", user_name);
            params.put("gender", "string");
            params.put("userNo", editTextId.getText().toString());
            params.put("userPhoto",imagePath);
            params.put("userEmail", editTextMail.getText().toString());
            params.put("userPassword", "string");
            params.put("userHometown", editTextHomeTown.getText().toString());
            params.put("userCurrentCity",editTextCurrentCity.getText().toString());
            params.put("userPhoneNum", editTextPhone.getText().toString());
            params.put("facebookId", "string");
            params.put("userAccessToken", "string");
            params.put("userRigisDate", "2016-05-26T02:01:36.409Z");

            // String url = "http://10.0.3.2:8080/ChatAskhmer/api/user/updateuser";
            String url = API.UPDATEUSER;
            GsonObjectRequest jsonRequest = new GsonObjectRequest(Request.Method.PUT, url, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("STATUS")) {
                            //success
                            success();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(UserProfile.this, "Unsuccessfully Edited !!", Toast.LENGTH_LONG).show();
                    } finally {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getBaseContext(), "ERROR_MESSAGE_NO_REPONSE: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
        } catch (JSONException e) {
            Toast.makeText(UserProfile.this, "ERROR_MESSAGE_JSONOBJECT" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(UserProfile.this, "ERROR_MESSAGE_EXP" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // sweet alert for success
    public void success(){
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Your Data was saved!!")
                .show();
    }


}

package com.askhmer.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.askhmer.chat.R;
import com.askhmer.chat.fragments.FourFragment;
import com.askhmer.chat.listener.OnSwipeTouchListener;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGE=100;
    private ImageView imageView;
    private File tempOutPutFile;

    private EditText editTextId;
    private EditText editTextPhone;
    private EditText editTextMail;
    private EditText editTextCurrentCity;
    private EditText editTextHomeTown;

    private ImageButton editId;
    private ImageButton editPhone;
    private ImageButton editMail;
    private ImageButton editHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sok Lundy");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImageView();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView)findViewById(R.id.imageView);
        tempOutPutFile = new File(getExternalCacheDir(),"tem-image.jpg");

        editTextId = (EditText)findViewById(R.id.editText_id);
        editTextPhone = (EditText)findViewById(R.id.editText_phone);
        editTextMail = (EditText)findViewById(R.id.editText_email);
        editTextCurrentCity = (EditText)findViewById(R.id.editText_current_city);
        editTextHomeTown = (EditText)findViewById(R.id.editText_hometown);

        editId = (ImageButton)findViewById(R.id.edit_id);
        editPhone = (ImageButton)findViewById(R.id.edit_phone);
        editMail = (ImageButton)findViewById(R.id.edit_email);
        editHome = (ImageButton)findViewById(R.id.edit_home);

        editId.setOnClickListener(editIdClick);
        editPhone.setOnClickListener(editPhoneClick);
        editMail.setOnClickListener(editMailClick);
        editHome.setOnClickListener(editHomeClick);

        View viUser = (View) findViewById(R.id.vi_user_1);
        AppBarLayout viUser1 = (AppBarLayout) findViewById(R.id.app_bar);

        viUser.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
            @Override
            public void onSwipeLeft() {
                finish();
            }
        });

        viUser1.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
            @Override
            public void onSwipeLeft() {
                finish();
            }
        });

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
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void changeImageView(){
        List<Intent> otherImageCaptureIntents = new ArrayList<>();
        List<ResolveInfo> otherImageCaptureActivities = getPackageManager()
                .queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),0);

        for (ResolveInfo info : otherImageCaptureActivities ) {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName,info.activityInfo.name);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempOutPutFile));
            otherImageCaptureIntents.add(captureIntent);
        }

        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");

        Intent choose = Intent.createChooser(selectImageIntent,"Chooser Avatar");
        choose.putExtra(Intent.EXTRA_INITIAL_INTENTS, otherImageCaptureIntents.toArray(new Parcelable[otherImageCaptureActivities.size()]));
        startActivityForResult(choose,REQUEST_SELECT_IMAGE);
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != RESULT_OK) {
            tempOutPutFile.delete();
            return;
        }

        if (requestCode == REQUEST_SELECT_IMAGE){
            Uri outputFile;
            Uri tempFileUri = Uri.fromFile(tempOutPutFile);

            if (data != null && (data.getAction() == null || !data.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE))) {
                outputFile = data.getData();
            }else{
                outputFile = tempFileUri;
            }
            new Crop(outputFile)
                    .asSquare()
                    .output(tempFileUri)
                    .start(this);
        }else if (requestCode == Crop.REQUEST_CROP) {
            imageView.setImageResource(0);
            imageView.setImageURI(Uri.fromFile(tempOutPutFile));
        }
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener editIdClick = new View.OnClickListener() {
        public void onClick(View v) {
           editTextAction(editTextId);
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
            editTextAction(editTextCurrentCity, editTextHomeTown);
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

}

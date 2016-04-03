package com.askhmer.chat.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.askhmer.chat.R;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGE=100;
    private ImageView imageView;
    private File tempOutPutFile;
    private EditText editText;
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











}

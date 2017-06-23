package com.xaaolaf.opencvdemo.secondsight;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.xaaolaf.opencvdemo.R;

/**
 * Created by xupingwei on 2017/6/23.
 */

public class LabActivity extends AppCompatActivity {

    public static final String PHOTO_MIME_TYPE = "image/png";
    public static String EXTRA＿PHOTO_URI = "com.xaaolaf.opencvdemo.secondsight.LabActivity.extra.PHOTO_URI";
    public static String EXTRA_PHOTO_DATA_PATH = "com.xaaolaf.opencvdemo.secondsight.LabActivity.extra.PHOTO_DATA_PATH";

    private Uri mUri;
    private String mDataPath;


    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);
        imageView = (ImageView) findViewById(R.id.lab_image);

        Intent intent = getIntent();
        mUri = intent.getParcelableExtra(EXTRA＿PHOTO_URI);
        mDataPath = intent.getStringExtra(EXTRA_PHOTO_DATA_PATH);
        imageView.setImageURI(mUri);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                deletePhoto();
                return true;
            case R.id.menu_edit:
                editPhoto();
                return true;
            case R.id.menu_share:
                sharePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sharePhoto() {

    }

    private void editPhoto() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setDataAndType(mUri, PHOTO_MIME_TYPE);
        startActivity(Intent.createChooser(intent, getString(R.string.photo_edit_chooser_title)));
    }

    private void deletePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.photo_delete_prompt_title);
        builder.setMessage(R.string.photo_delete_prompt_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.MediaColumns.DATA + "=?", new String[]{mDataPath});
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }
}

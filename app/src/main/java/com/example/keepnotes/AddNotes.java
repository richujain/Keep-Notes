package com.example.keepnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddNotes extends AppCompatActivity {

    private EditText title,description;
    private Button uploadImage,saveNote;
    ImageView uploadedImage;
    public static SQLiteHelper sqLiteHelper;
    private static final int NUM_COLUMNS = 2;

    private String titleOfNote = "11";
    private String descriptionOfNote = "11";
    private String lat = "132", lon = "123", datetime = "465";
    private int categoryId;
    //
    //user input
    private static final int GALLERY_REQUEST_CODE = 1889;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        title = findViewById(R.id.noteTitle);
        description = findViewById(R.id.description);
        uploadImage = findViewById(R.id.uploadImage);
        saveNote = findViewById(R.id.saveNote);
        uploadedImage = findViewById(R.id.imageUploaded);
        Intent mIntent = getIntent();
        categoryId = mIntent.getIntExtra("categoryId", 0);

        //sqlite
        sqLiteHelper = new SQLiteHelper(this, "TasksDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS NOTES(ID INTEGER PRIMARY KEY AUTOINCREMENT,cid integer, title VARCHAR(100), description varchar(500),image BLOB, lat varchar(15),lon varchar(15),datetime varchar(15))");

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToDatabase();
            }
        });

    }
    private void saveDataToDatabase(){
        titleOfNote = title.getText().toString().trim();
        descriptionOfNote = description.getText().toString().trim();

        sqLiteHelper.insertData(
                categoryId,titleOfNote,descriptionOfNote,imageViewToByte(uploadedImage),lat,lon,datetime
        );
        Toast.makeText(this, "Saving Note...", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Note Added Successfully. ", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddNotes.this, NotesList.class));
        Intent myIntent = new Intent(AddNotes.this, NotesList.class);
        myIntent.putExtra("categoryId", categoryId);
        startActivity(myIntent);
    }
    //adding category
    private void pickFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);

    }
    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    this.uploadedImage.setImageURI(selectedImage);
                    uploadedImage.setVisibility(View.VISIBLE);
                    break;
            }

    }
    private byte[] imageViewToByte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return  byteArray;
    }


}

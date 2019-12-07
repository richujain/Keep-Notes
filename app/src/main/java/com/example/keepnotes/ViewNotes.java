package com.example.keepnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;

public class ViewNotes extends AppCompatActivity {

    private EditText title,description;
    private Button uploadImage,saveNote;
    private ImageView uploadedImage;
    private Integer noteId;
    TextView date,location;
    public static SQLiteHelper sqLiteHelper;

    private String titleOfNote = "11";
    private String descriptionOfNote = "11";
    private byte[] image;
    String lat = "", lon = "", datetime = "";

    private static final int GALLERY_REQUEST_CODE = 1889;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        title = findViewById(R.id.noteTitleName);
        description = findViewById(R.id.description);
        uploadImage = findViewById(R.id.uploadImage);
        saveNote = findViewById(R.id.saveNote);
        uploadedImage = findViewById(R.id.imageUploadedEdit);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        Intent mIntent = getIntent();
        noteId = mIntent.getIntExtra("noteId", 0);
        Log.d("noteId: ",""+noteId);
        fetchData();
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

        sqLiteHelper.updateData(
                noteId,titleOfNote,descriptionOfNote,imageViewToByte(uploadedImage)
        );
        Toast.makeText(this, "Saving Note...", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Note Added Successfully. ", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ViewNotes.this, NotesList.class));
        Intent myIntent = new Intent(ViewNotes.this, CategoryList.class);
        finish();
        startActivity(myIntent);
    }

    private void fetchData(){

        sqLiteHelper = new SQLiteHelper(this, "TasksDB.sqlite", null, 1);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String sql = "SELECT * FROM NOTES where id = "+noteId;
        Cursor cursor = db.rawQuery(sql, new String[] {});
        if(cursor.moveToFirst()) {
            do{

                String titleName,description;
                titleName = cursor.getString(2);
                description = cursor.getString(3);
                image = cursor.getBlob(4);
                lat = cursor.getString(5);
                lon = cursor.getString(6);
                datetime = cursor.getString(7);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background);

                Glide.with(this)
                        .load(image)
                        .apply(requestOptions)
                        .into(uploadedImage);
                uploadedImage.setVisibility(View.VISIBLE);
                this.title.setText(titleName);
                this.description.setText(description);
                this.location.setText("Lat: "+ lat + ", Lon: " + lon);
                this.date.setText("DATETIME: "+datetime);

            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
    }



    //adding category
    private void pickFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        //Launching the Intent
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

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(ViewNotes.this,CategoryList.class));
    }
}

package com.example.keepnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
public class CategoryList extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    private static final String TAG = "MainActivity";
    private static final int NUM_COLUMNS = 2;

    private ArrayList<byte[]> mImages = new ArrayList<byte[]>();
    private ArrayList<String> mNames = new ArrayList<>();
    //
    ArrayList<Integer> mId = new ArrayList<>();
    //user input
    ImageView imageView;
    String categoryName = "";
    private static final int GALLERY_REQUEST_CODE = 1889;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);


        sqLiteHelper = new SQLiteHelper(this, "TasksDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS CATEGORY(ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,image BLOB)");
        initImageBitmaps();
    }

    private void initImageBitmaps(){
        fetchData();
        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: initializing staggered recyclerview.");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CategoryAdapter staggeredRecyclerViewAdapter =
                new CategoryAdapter(this, mId, mNames, mImages);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }
    //adding menu buttons
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuforcategorylist, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                //startActivity(new Intent(this, About.class));
                return true;
            case R.id.help:
                //startActivity(new Intent(this, Help.class));
                return true;
            case R.id.add:
                addCategory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    this.imageView.setImageURI(selectedImage);
                    break;
            }

    }

    private void fetchData(){
        mImages.clear();
        mNames.clear();

        sqLiteHelper = new SQLiteHelper(this, "TasksDB.sqlite", null, 1);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String sql = "SELECT * FROM CATEGORY";
        Cursor cursor = db.rawQuery(sql, new String[] {});
        if(cursor.moveToFirst()) {
            do{
                this.mId.add(cursor.getInt(0));
                this.mNames.add(cursor.getString(1));
                this.mImages.add(cursor.getBlob(2));
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
    }

    private void addCategory(){
        categoryName = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Category");

// Set up the input
        final EditText input = new EditText(this);
        imageView = new ImageView(this);
        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(input);
        ll.addView(imageView);
        //final ImageView imageView = new ImageView(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        imageView.setImageResource(R.drawable.plus);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(ll);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryName = input.getText().toString().trim();
                //insert
                try{
                    sqLiteHelper.insertData(
                            categoryName,imageViewToByte(imageView)
                    );
                    Toast.makeText(CategoryList.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    initImageBitmaps();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
        finishAffinity();
        finish();
    }
}

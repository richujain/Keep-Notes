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
public class NotesList extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    private static final int NUM_COLUMNS = 2;

    private ArrayList<byte[]> mImages = new ArrayList<byte[]>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    //
    ArrayList<Integer> mId = new ArrayList<>();
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        Intent mIntent = getIntent();
        categoryId = mIntent.getIntExtra("categoryId", 0);
        sqLiteHelper = new SQLiteHelper(this, "TasksDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS NOTES(ID INTEGER PRIMARY KEY AUTOINCREMENT,cid integer, title VARCHAR(100), description varchar(500),image BLOB, lat varchar(15),lon varchar(15),datetime varchar(15))");

        initImageBitmaps();
    }

    private void initImageBitmaps(){
        fetchData();
        initRecyclerView();

    }

    private void initRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.recyclerViewList);
        NotesAdapter staggeredRecyclerViewAdapter =
                new NotesAdapter(this, mId, mNames,mDescription, mImages);
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
                startActivity(new Intent(NotesList.this,AddNotes.class));
                Intent myIntent = new Intent(NotesList.this, AddNotes.class);
                myIntent.putExtra("categoryId", categoryId);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetchData(){
        //mImages.clear();
        //mNames.clear();
        //mDescription.clear();

        sqLiteHelper = new SQLiteHelper(this, "TasksDB.sqlite", null, 1);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String sql = "SELECT * FROM NOTES where cid = "+categoryId;
        Cursor cursor = db.rawQuery(sql, new String[] {});
        if(cursor.moveToFirst()) {
            do{

                Log.e("from db"+cursor.getColumnName(1),""+cursor.getInt(1));
                /*Log.e(""+cursor.getColumnName(2),""+cursor.getString(2));*/
                this.mId.add(cursor.getInt(0));
                this.mNames.add(cursor.getString(2));
                this.mDescription.add(cursor.getString(3));
                this.mImages.add(cursor.getBlob(4));

            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
    }






}

package com.laika.laika_yellow_book;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResults extends AppCompatActivity {

    DbHelper myDb;
    ListView listview;

    CowSearchedAdapter cowSearchedAdapter;
    Context ctx;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        myDb = new DbHelper(this);
        Cursor cursor;


        //Bringing in the text from main activity search box
        //Displaying it in the box on this page
        TextView resultsDisplay = null;
        String text = "";
        if (getIntent().hasExtra("com.laika.laika_yellow_book.SearchContent")) {
            EditText searchBox = findViewById(R.id.editTextSearch2);
            text = getIntent().getExtras().getString("com.laika.laika_yellow_book.SearchContent");
            searchBox.setText(text);

            getSearchResults(text);

        }

    }

    public void getSearchResults(String text) {
        listview = (ListView) activity.findViewById(R.id.display_listview);
        Cursor cursor = myDb.getDataByID(text);
        cowSearchedAdapter = new CowSearchedAdapter(ctx,R.layout.display_search_result_row);
        int cowNum, sire, calfID;

        while (cursor.moveToNext()) {
            cowNum = cursor.getInt(cursor.getColumnIndex("CowNum"));
            sire = cursor.getInt(cursor.getColumnIndex("SireOfCalf"));
            calfID = cursor.getInt(cursor.getColumnIndex("CalfID"));

            CowSearched cowSearched = new CowSearched(cowNum,sire,calfID);
            cowSearchedAdapter.add(cowSearched);
        }

        listview.setAdapter(cowSearchedAdapter);

    }

}



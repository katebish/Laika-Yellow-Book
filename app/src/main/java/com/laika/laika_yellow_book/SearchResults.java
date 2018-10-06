package com.laika.laika_yellow_book;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SearchResults extends AppCompatActivity {

    DbHelper myDb;
    ListView listview;

    CowSearchedAdapter cowSearchedAdapter;

    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        EditText searchBox = findViewById(R.id.editTextSearch2);

        //Get search Input from Main Activity
        if (getIntent().hasExtra("com.laika.laika_yellow_book.SearchContent")) {
            text = getIntent().getExtras().getString("com.laika.laika_yellow_book.SearchContent");
            searchBox.setText(text);
            getSearchResults(text);
        }


        final Button button = findViewById(R.id.buttonGo2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText searchBox = findViewById(R.id.editTextSearch2);
                text = searchBox.getText().toString();
                getSearchResults(text);
            }
        });

    }


    public void getSearchResults(String text) {
        myDb = new DbHelper(this);
        listview = (ListView) findViewById(R.id.display_listview);
        //listview.setOnItemClickListener(this);
        Cursor cursor = myDb.getDataByID(text);
        cowSearchedAdapter = new CowSearchedAdapter(this,R.layout.display_search_result_row);
        int cowNum, sire, calfID, rowID;

        while (cursor.moveToNext()) {
            cowNum = cursor.getInt(cursor.getColumnIndex("CowNum"));
            sire = cursor.getInt(cursor.getColumnIndex("SireOfCalf"));
            calfID = cursor.getInt(cursor.getColumnIndex("CalfID"));
            rowID = cursor.getInt(cursor.getColumnIndex("ID"));

            CowSearched cowSearched = new CowSearched(cowNum,sire,calfID,rowID);
            cowSearchedAdapter.add(cowSearched);
        }

        listview.setAdapter(cowSearchedAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                CowSearched value = (CowSearched) listview.getItemAtPosition(position);
                //int row_id = value.getRowID();
                String row_id = Integer.toString(value.getRowID());

                UpdateDeletePage(view, row_id);
            }
        });

    }

    public void UpdateDeletePage(View view, String row_id) {
        Intent intent = new Intent(this, NewEntryActivity.class);
        intent.putExtra("com.laika.laika_yellow_book.rowIDContent", row_id);
        finish();
        startActivity(intent);
    }

}



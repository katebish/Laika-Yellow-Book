package com.laika.laika_yellow_book;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SearchResults extends AppCompatActivity {

    DbHelper myDb, myDbAuto;
    ListView listview;

    CowSearchedAdapter cowSearchedAdapter;

    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        AutoCompleteTextView searchBox = findViewById(R.id.autoCompleteTextViewSearch);

        myDbAuto = new DbHelper(this);

        String[] cIds = myDbAuto.returnIds();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, cIds);

        searchBox.setThreshold(1);
        searchBox.setAdapter(adapter);

        //create toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        mToolbar.setTitle("Search Results");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Get search Input from Main Activity
        if (getIntent().hasExtra("com.laika.laika_yellow_book.SearchContent")) {
            text = getIntent().getExtras().getString("com.laika.laika_yellow_book.SearchContent");
            searchBox.setText(text);
            searchBox.dismissDropDown();
            getSearchResults(text);
        }


        final Button button = findViewById(R.id.buttonGo2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AutoCompleteTextView searchBox = findViewById(R.id.autoCompleteTextViewSearch);
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

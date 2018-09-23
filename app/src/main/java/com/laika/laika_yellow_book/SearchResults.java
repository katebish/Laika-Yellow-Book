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
    Context context;
    CowSearchedAdapter cowSearchedAdapter;
    Activity activity;

    ListView listView;
    private DbHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        myDb = new DbHelper(this);

        //Bringing in the text from main activity search box
        //Displaying it in the box on this page
        TextView resultsDisplay = null;
        String text = "";
        if (getIntent().hasExtra("com.laika.laika_yellow_book.SearchContent")){
            resultsDisplay = (TextView) findViewById(R.id.searchResBox);
            EditText searchBox = findViewById(R.id.editTextSearch2);
            text = getIntent().getExtras().getString("com.laika.laika_yellow_book.SearchContent");
            searchBox.setText(text);
            getSearchResults(text);
        }


    }

//        Cursor result = myDb.getDataByID(text);
//        while(result.moveToNext()) {
//            int CowNumb = result.getColumnIndex("CowNum");
//            int Sire = result.getColumnIndex("SireOfCalf");
//            int CalfID = result.getColumnIndex("CalfID");
//            String cowRes = result.getString(CowNumb);
//            String sireRes = result.getString(Sire);
//            String calfRes = result.getString(CalfID);
//            resultsDisplay.setText("CowNum: " + cowRes + "\nSire: " + sireRes + "\nCalfID: " + calfRes);
//        }

//        Cursor cursor = myDb.getDataByIDCowNum(text);
//        while(cursor.moveToNext()) {
//            int CowNumb = cursor.getColumnIndex("CowNum");
//            int cowRes = cursor.getInt(CowNumb);
//            resultsDisplay.setText("CowNum: " + cowRes);
//        }

        public void getSearchResults(String text) {
            Cursor cursor = myDb.getDataByIDCowNum(text);
            cowSearchedAdapter = new CowSearchedAdapter(context, R.layout.display_search_result_row);
            int cowNum, sire, calfID;
            while (cursor.moveToNext()) {
                cowNum = cursor.getInt(cursor.getColumnIndex("CowNum"));
                sire = cursor.getInt(cursor.getColumnIndex("SireOfCalf"));
                calfID = cursor.getInt(cursor.getColumnIndex("CalfID"));
                CowSearched cowSearched = new CowSearched(cowNum, sire, calfID);
                cowSearchedAdapter.add(cowSearched);
                displayResultsList();
            }
        }

        private void displayResultsList()
        {
            listView = (ListView) activity.findViewById(R.id.display_listview);
            listView.setAdapter(cowSearchedAdapter);
        }




}

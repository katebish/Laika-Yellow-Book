package com.laika.laika_yellow_book;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MostRecent extends AppCompatActivity {

    DbHelper myDb;
    ListView listview;

    CowSearchedAdapter cowSearchedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_recent);

        myDb = new DbHelper(this);

        listview = (ListView) findViewById(R.id.display_most_recent_listview);
        Cursor cursor = myDb.getMostRecentData();
        cowSearchedAdapter = new CowSearchedAdapter(this,R.layout.display_search_result_row);
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

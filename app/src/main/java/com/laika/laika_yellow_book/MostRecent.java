package com.laika.laika_yellow_book;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MostRecent extends AppCompatActivity {

    DbHelper myDb;
    ListView listview;

    CowSearchedAdapter cowSearchedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_recent);

        //create toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mostRecent_toolbar);
        mToolbar.setTitle("Most Recent Entries");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myDb = new DbHelper(this);

        listview = (ListView) findViewById(R.id.display_most_recent_listview);
        Cursor cursor = myDb.getMostRecentData();
        cowSearchedAdapter = new CowSearchedAdapter(this,R.layout.display_search_result_row);
        int cowNum, sire, calfID, id;

        while (cursor.moveToNext()) {
            cowNum = cursor.getInt(cursor.getColumnIndex("CowNum"));
            sire = cursor.getInt(cursor.getColumnIndex("SireOfCalf"));
            calfID = cursor.getInt(cursor.getColumnIndex("CalfID"));
            id = cursor.getInt(cursor.getColumnIndex("ID"));

            CowSearched cowSearched = new CowSearched(cowNum,sire,calfID,id);
            cowSearchedAdapter.add(cowSearched);
        }

        listview.setAdapter(cowSearchedAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CowSearched value = (CowSearched) listview.getItemAtPosition(position);
                String row_id = Integer.toString(value.getRowID());
                UpdateDeletePage(view, row_id);
            }
        });
    }

    public void UpdateDeletePage(View view, String row_id) {
        Intent intent = new Intent(this, NewEntryActivity.class);
        intent.putExtra("com.laika.laika_yellow_book.rowIDContent", row_id);
        startActivity(intent);
    }
}

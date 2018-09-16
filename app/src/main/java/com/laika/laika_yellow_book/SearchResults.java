package com.laika.laika_yellow_book;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

public class SearchResults extends AppCompatActivity {
    private DbHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        myDb = new DbHelper(this);
        TextView resultsDisplay = null;
        String text = "";
        if (getIntent().hasExtra("com.laika.laika_yellow_book.SearchContent")){
            resultsDisplay = (TextView) findViewById(R.id.searchResBox);
            EditText searchBox = findViewById(R.id.editTextSearch2);
            text = getIntent().getExtras().getString("com.laika.laika_yellow_book.SearchContent");
            searchBox.setText(text);
        }

        Cursor result = myDb.getDataByID(text);
        while(result.moveToNext()) {
            int ID = result.getColumnIndex("ID");
            resultsDisplay.setText(ID + " ");
        }
    }

}

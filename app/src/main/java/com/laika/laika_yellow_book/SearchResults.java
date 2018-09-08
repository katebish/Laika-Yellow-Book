package com.laika.laika_yellow_book;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class SearchResults extends AppCompatActivity {
    private DbHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        myDb = new DbHelper(this);

        if (getIntent().hasExtra("com.laika.laika_yellow_book.SearchContent")){
            //TextView resultsDisplay = (TextView) findViewById(R.id.searchResBox);
            EditText searchBox = (EditText) findViewById(R.id.editTextSearch2);
            String text = getIntent().getExtras().getString("com.laika.laika_yellow_book.SearchContent");
            searchBox.setText(text);
        }



    }

}

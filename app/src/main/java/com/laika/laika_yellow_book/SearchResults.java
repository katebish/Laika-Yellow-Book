package com.laika.laika_yellow_book;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SearchResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        if (getIntent().hasExtra("com.laika.laika_yellow_book.SearchContent")){
            TextView resultsDisplay = (TextView) findViewById(R.id.searchResBox);
            String text = getIntent().getExtras().getString("com.laika.laika_yellow_book.SearchContent");
            resultsDisplay.setText(text);
        }



    }

}

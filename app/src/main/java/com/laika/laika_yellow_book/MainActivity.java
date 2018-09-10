package com.laika.laika_yellow_book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void NewEntryPage(View view) {
        Intent intent = new Intent(this, NewEntryActivity.class);
        startActivity(intent);
    }

    public void SearchResults(View view) {
        Intent intent = new Intent(this, SearchResults.class);

        EditText textSearchBox = (EditText) findViewById(R.id.editTextSearch);
        String searchContent = textSearchBox.getText().toString();
        //pass search box information to searchResults activity
        intent.putExtra("com.laika.laika_yellow_book.SearchContent", searchContent);

        startActivity(intent);
    }
}

package com.laika.laika_yellow_book;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DbHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onResume(){
        super.onResume();
        myDb = new DbHelper(this);

        String[] cIds = myDb.returnIds();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, cIds);

        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
        Log.d("test", "updated autocomplete");
    }

    public void NewEntryPage(View view) {
        Intent intent = new Intent(this, NewEntryActivity.class);
        startActivity(intent);
    }

    public void SearchResults(View view) {
        Intent intent = new Intent(this, SearchResults.class);

        EditText textSearchBox = (EditText) findViewById(R.id.autoCompleteTextView);
        String searchContent = textSearchBox.getText().toString();
        //pass search box information to searchResults activity
        intent.putExtra("com.laika.laika_yellow_book.SearchContent", searchContent);

        startActivity(intent);
    }

}

package com.laika.laika_yellow_book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

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

        EditText textSearchBox = findViewById(R.id.autoCompleteTextView);
        textSearchBox.setText("");

        myDb = new DbHelper(this);

        String[] cIds = myDb.returnIds();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, cIds);

        AutoCompleteTextView actv = findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
    }

    public void NewEntryPage(View view) {
        Intent intent = new Intent(this, NewEntryActivity.class);
        startActivity(intent);
    }

    public void MostRecent(View view) {
        Intent intent = new Intent(this, MostRecent.class);
        startActivity(intent);
    }

    public void SearchResults(View view) {
        Intent intent = new Intent(this, SearchResults.class);

        EditText textSearchBox = findViewById(R.id.autoCompleteTextView);
        String searchContent = textSearchBox.getText().toString();
        intent.putExtra("com.laika.laika_yellow_book.SearchContent", searchContent);
        startActivity(intent);
    }
}

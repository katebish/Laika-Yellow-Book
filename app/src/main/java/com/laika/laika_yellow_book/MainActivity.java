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

        EditText textSearchBox = (EditText) findViewById(R.id.autoCompleteTextView);
        textSearchBox.setText("");

        myDb = new DbHelper(this);

        String[] cIds = myDb.returnIds();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, cIds);

        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);
        actv.setAdapter(adapter);

        //Inserting some test data on load, used to test search function
//        try {
//            String calDate = "2018-09-11";
//            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(calDate);
//
//            myDb.insertData(32, null, 35, 0, date1, "k", null, null, null, 0, null);
//            myDb.insertData(658, null, 32, 0, date1, null, null, null, null, 116, null);
//            myDb.insertData(632, null, 32, 0, date1, null, null, null, null, 119, null);
//            myDb.insertData(836, null, 32, 0, date1, null, null, null, null, 125, null);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

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

        EditText textSearchBox = (EditText) findViewById(R.id.autoCompleteTextView);
        String searchContent = textSearchBox.getText().toString();
        //pass search box information to searchResults activity
        intent.putExtra("com.laika.laika_yellow_book.SearchContent", searchContent);
        startActivity(intent);
    }
}

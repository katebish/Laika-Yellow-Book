package com.laika.laika_yellow_book;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {

    Context context;
    DbHelper myDb;

    MySimpleArrayAdapter adapter;
    //CowSearchedAdapter adapter;

    ArrayList<CowSearched> listItems;

    ArrayList<String> newData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ListView listView = (ListView) findViewById(R.id.display_listview);
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

            listItems = myDb.getSearchResults(text);


            newData = new ArrayList<String>();

            for (CowSearched note : listItems) {
                String cowNum = Integer.toString(note.cowNum);
                newData.add(cowNum);
            }

            adapter = new MySimpleArrayAdapter(this, newData);

            // Assigning the adapter to ListView
            listView.setAdapter(adapter);

//            // Assigning an event to the listview
//            // This event will be used to delete records
//            listView.setOnItemLongClickListener(myClickListener);

        }



    }

    /**
     * This adapter will create your list view row by row
     */
    public class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values;

        public MySimpleArrayAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.display_search_result_row, values);

            this.context = context;
            this.values = values;
        }

        /**
         * Here we go and get our rowlayout.xml file and set the textview text.
         * This happens for every row in your listview.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.display_search_result_row, parent, false);

            TextView textView = (TextView) rowView.findViewById(R.id.t_cowNum);

            // Setting the text to display
            textView.setText(values.get(position));

            return rowView;
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

//        public void getSearchResults(String text) {
//            Cursor cursor = myDb.getDataByIDCowNum(text);
//
//            cowSearchedAdapter = new CowSearchedAdapter(context, R.layout.display_search_result_row);
//            int cowNum, sire, calfID;
//            while (cursor.moveToNext()) {
//                cowNum = cursor.getInt(cursor.getColumnIndex("CowNum"));
//                sire = cursor.getInt(cursor.getColumnIndex("SireOfCalf"));
//                calfID = cursor.getInt(cursor.getColumnIndex("CalfID"));
//                CowSearched cowSearched = new CowSearched(cowNum, sire, calfID);
//                cowSearchedAdapter.add(cowSearched);
//                displayResultsList();
//            }
//        }


//    public ArrayList<CowSearched> getSearchResults(String text) {
//
//        ArrayList<CowSearched> listItems = new ArrayList<CowSearched>();
//
//        Cursor cursor = myDb.getDataByIDCowNum(text);
//
//        if (cursor.moveToFirst()) {
//            do {
//                CowSearched searchInput = new CowSearched();
//
//                searchInput.cowNum = cursor.getInt(cursor.getColumnIndex("CowNum"));
//                searchInput.sire = cursor.getInt(cursor.getColumnIndex("SireOfCalf"));
//                searchInput.calfID = cursor.getInt(cursor.getColumnIndex("CalfID"));
//
//                listItems.add(searchInput);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//
//        return listItems;
//    }


//        private void displayResultsList()
//        {
//            listView = (ListView) activity.findViewById(R.id.display_listview);
//            listView.setAdapter(cowSearchedAdapter);
//        }




}

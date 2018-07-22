package com.laika.laika_yellow_book;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewEntryActivity extends AppCompatActivity {
    DbHelper myDb;
    EditText edit_CowNumber, edit_DueCalveDate, edit_SireIfCalf, edit_CalfBW, edit_CalvingDate, edit_CalvingDiff, edit_Condition, edit_Sex, edit_Fate, edit_CalfIndent,edit_Remarks;
    Button btn_Save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        myDb = new DbHelper(this);

        edit_CowNumber = (EditText) findViewById(R.id.edit_CowNumber);
        edit_DueCalveDate = (EditText) findViewById(R.id.edit_DueCalveDate);
        edit_SireIfCalf = (EditText) findViewById(R.id.edit_SireIfCalf);
        edit_CalfBW = (EditText) findViewById(R.id.edit_CalfBW);
        edit_CalvingDate = (EditText) findViewById(R.id.edit_CalvingDate);
        edit_CalvingDiff = (EditText) findViewById(R.id.edit_CalvingDiff);
        edit_Condition = (EditText) findViewById(R.id.edit_Condition);
        edit_Sex = (EditText) findViewById(R.id.edit_Sex);
        edit_Fate = (EditText) findViewById(R.id.edit_Fate);
        edit_CalfIndent = (EditText) findViewById(R.id.edit_CalfIndent);
        edit_Remarks = (EditText) findViewById(R.id.edit_Remarks);
        btn_Save = (Button) findViewById(R.id.btn_Save);
    }

    public void AddData(View view) throws ParseException {
        int cn = Integer.parseInt(edit_CowNumber.getText().toString());
        String d =  edit_DueCalveDate.getText().toString();
        Date dcd = new SimpleDateFormat("dd/MM/yyyy").parse(d);
        String c = edit_CalvingDate.getText().toString();
        Date cd = new SimpleDateFormat("dd/MM/yyyy").parse(c);
        int soc = Integer.parseInt(edit_SireIfCalf.getText().toString());
        double cbw = Double.parseDouble(edit_CalfBW.getText().toString());
        int cin = Integer.parseInt(edit_CalfIndent.getText().toString());
        boolean isSuccessful = myDb.insertData(cn,dcd,soc,cbw,cd,edit_CalvingDiff.getText().toString(),edit_Condition.getText().toString(),edit_Sex.getText().toString(),edit_Fate.getText().toString(),cin,edit_Remarks.getText().toString());
        if(isSuccessful)
           Toast.makeText(NewEntryActivity.this,"Data is inserted",Toast.LENGTH_LONG).show();
        else
           Toast.makeText(NewEntryActivity.this,"Insertion failed",Toast.LENGTH_LONG).show();
    }
}

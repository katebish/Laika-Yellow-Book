package com.laika.laika_yellow_book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

    public void SpeechTest(View view) {
        Intent intent = new Intent(this, SpeechTest.class);
        startActivity(intent);
    }
}

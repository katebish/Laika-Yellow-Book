package com.laika.laika_yellow_book;


import android.content.ActivityNotFoundException;
import android.speech.RecognitionListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.*;

//Basic speech to text
public class SpeechTest extends AppCompatActivity implements RecognitionListener{

    //Request code for speech input
    private final int REQ_CODE_SPEECH_INPUT = 1;
    //to be used to no dialog speech recognition
    //in progress
    private final int REQ_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechtest);
    }
    //Starts android speech on button click
    public void startSpeech(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        //offline mode
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException a) { }
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);

        //switch on request code
        switch(req){
            case REQ_CODE_SPEECH_INPUT: {
                //if result is successful
                //loads all data into an arraylist and outputs in the textview
                if(res == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    TextView textV = (TextView) findViewById(R.id.TextView);
                    String all = (String) textV.getText();
                    Iterator<String> i = result.iterator();
                    while(i.hasNext())
                    {
                        all +=" "+i.next();
                    }
                    textV.setText(all);
                }
            }
        }
    }
    @Override
    public void onEndOfSpeech() {
        //need to implement this
    }
    @Override
    public void onReadyForSpeech(Bundle savedInstanceState) {
        //need to implement this
    }
    @Override
    public void onBufferReceived(byte[] b) {
        //need to implement this
    }
    @Override
    public void onResults(Bundle savedInstanceState){
        //need to implement this
    }
    @Override
    public void onError(int error){
        //need to implement
    }
    @Override
    public void onRmsChanged(float rms){
        //need to implement
    }
    @Override
    public void onEvent(int bundle, Bundle savedInstanceState){
        //need to implement
    }
    @Override
    public void onBeginningOfSpeech(){
        //need to implement
    }
    @Override
    public void onPartialResults(Bundle savedInstanceState){
        //need to implement
    }
}




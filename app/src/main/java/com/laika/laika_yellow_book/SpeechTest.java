package com.laika.laika_yellow_book;


import android.content.ActivityNotFoundException;
import android.speech.RecognitionListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

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
                    textV.setText(textV.getText()+"result of curl"+validateString(result));
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
    private String validateString(ArrayList<String> result) {
        try {
            String cleaned = "";
            URL request = new URL("https://api.wit.ai/message?v=20180801&q=" + result.get(0));
            HttpURLConnection connection = (HttpURLConnection) request.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer JL26524NMPCGBFR7253SR5K2DWKUHXUW");
            if (connection.getResponseCode() == 200) {
                InputStream input = connection.getInputStream();
                InputStreamReader inputReader = new InputStreamReader(input);
                int data;
                while ((data = inputReader.read()) != -1) {
                    cleaned += data;
                }
            }
            connection.disconnect();
            return cleaned;
        } catch (IOException e) {
            Toast.makeText(SpeechTest.this, "Connection failed", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }
}




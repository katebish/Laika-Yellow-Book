package com.laika.laika_yellow_book;


import android.content.ActivityNotFoundException;
import android.os.AsyncTask;
import android.speech.RecognitionListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

//Basic speech to text
public class SpeechTest extends AppCompatActivity implements RecognitionListener {

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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //offline mode
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
        }
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);

        //switch on request code
        switch (req) {
            case REQ_CODE_SPEECH_INPUT: {
                //if result is successful
                //loads all data into an arraylist and outputs in the textview
                if (res == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    TextView textV = (TextView) findViewById(R.id.TextView);
                    String all = (String) textV.getText();
                    Iterator<String> i = result.iterator();
                    while (i.hasNext()) {
                        all += " " + i.next();
                    }
                    //textV.setText(all);
                    ValidateResults test= new ValidateResults();
                    test.execute(result);
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
    public void onResults(Bundle savedInstanceState) {
        //need to implement this
    }

    @Override
    public void onError(int error) {
        //need to implement
    }

    @Override
    public void onRmsChanged(float rms) {
        //need to implement
    }

    @Override
    public void onEvent(int bundle, Bundle savedInstanceState) {
        //need to implement
    }

    @Override
    public void onBeginningOfSpeech() {
        //need to implement
    }

    @Override
    public void onPartialResults(Bundle savedInstanceState) {
        //need to implement
    }
    private class ValidateResults extends AsyncTask<ArrayList<String>, Void, String> {

        @Override

        protected String doInBackground(ArrayList<String>... results) {
            String cleaned = "";
            try {
                URL request = new URL("https://api.wit.ai/message?v=20180801&q="+results[0].get(0));
                HttpsURLConnection connection = (HttpsURLConnection) request.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer JL26524NMPCGBFR7253SR5K2DWKUHXUW");
                connection.setReadTimeout(10 * 1000);
                connection.connect();

                BufferedReader buffRead = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                String output = buffRead.readLine();
                if (output != null) {
                    cleaned = output;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cleaned;
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsob = new JSONObject(result);
                String text = jsob.getString("_text");
                TextView textV = (TextView) findViewById(R.id.TextView);
                textV.setText(text);
            }
            catch (Exception e){}

        }

    }
}




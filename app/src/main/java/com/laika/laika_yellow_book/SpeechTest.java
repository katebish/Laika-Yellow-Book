package com.laika.laika_yellow_book;


import android.content.ActivityNotFoundException;
import android.os.AsyncTask;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.util.Log;
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
public class SpeechTest extends AppCompatActivity {

    private SpeechRecognizer speechR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechtest);
        speechR = SpeechRecognizer.createSpeechRecognizer(this);
        speechR.setRecognitionListener(new SpeechListener());
    }

    //Starts android speech on button click
    public void startSpeech(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 2000);

        speechR.startListening(intent);
    }

    class SpeechListener implements RecognitionListener {

        @Override
        public void onEndOfSpeech() {
            //need to implement this
            Log.d("Speech End", "True");
        }

        @Override
        public void onReadyForSpeech(Bundle results) {
            //need to implement this
        }

        @Override
        public void onBufferReceived(byte[] b) {
            //need to implement this
        }

        @Override
        public void onResults(Bundle results) {
            TextView textV = (TextView) findViewById(R.id.TextView);
            String all = (String) textV.getText();
            all += " END";
            textV.setText(all);
        }

        @Override
        public void onError(int error) {
            //need to implement
            Log.d("Speech Error", Integer.toString(error));
        }

        @Override
        public void onRmsChanged(float rms) {
            //need to implement
        }

        @Override
        public void onEvent(int bundle, Bundle results) {
            //need to implement
        }

        @Override
        public void onBeginningOfSpeech() {
            //need to implement
            Log.d("Speech Start", "True");
        }

        @Override
        public void onPartialResults(Bundle results) {
            ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            TextView textV = (TextView) findViewById(R.id.TextView);
            if(!data.isEmpty()&&data.get(0).matches("(?i)submit")){
                speechR.stopListening();
                textV.setText("END?");
            }
            String all = (String) textV.getText();
            if(!data.isEmpty()) {
                all = " "+data.get(0);
            }
            textV.setText(all);
        }
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




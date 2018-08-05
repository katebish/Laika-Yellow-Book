package com.laika.laika_yellow_book;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;
import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;


public class NewEntryActivity extends AppCompatActivity{
    private DbHelper myDb;
    private TextToSpeech mTTS;
    private Button voiceInput;
    private LinearLayout layout;
    private int childCount;
    private String[] text;
    private int i = 0;
    private EditText currEditText;
    private int editPos = 0;
    private EditText[] editTexts;
    private DataFields cow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        myDb = new DbHelper(this);

        cow = new DataFields();
        editTexts = new EditText[11];
        editTexts[0] = (EditText) findViewById(R.id.edit_CowNumber);
        editTexts[1] = (EditText) findViewById(R.id.edit_CalfIndent);
        editTexts[2] = (EditText) findViewById(R.id.edit_DueCalveDate);
        editTexts[3] = (EditText) findViewById(R.id.edit_SireIfCalf);
        editTexts[4] = (EditText) findViewById(R.id.edit_CalfBW);
        editTexts[5] = (EditText) findViewById(R.id.edit_CalvingDate);
        editTexts[6] = (EditText) findViewById(R.id.edit_CalvingDiff);
        editTexts[7] = (EditText) findViewById(R.id.edit_Condition);
        editTexts[8] = (EditText) findViewById(R.id.edit_Sex);
        editTexts[9] = (EditText) findViewById(R.id.edit_Fate);
        editTexts[10] = (EditText) findViewById(R.id.edit_Remarks);
        voiceInput = (Button) findViewById(R.id.btn_VoiceInput);

        //get all textview values
        layout = (LinearLayout) findViewById(R.id.linearLayout1);
        childCount = layout.getChildCount();
        text = new String[childCount];
        int c = 0;
        for (int i = 0; i < childCount; i ++) {
            View v = layout.getChildAt(i);
            //EditText extends TextView
            if(v instanceof EditText) {
                continue;
            }
            else if(v instanceof TextView) {
                if(findViewById(R.id.tv_addTwinCalf)!=v) {
                    text[c] = ((TextView) v).getText().toString();
                    c++;
                }
            }
        }
        initTTS();
    }

    private void initTTS() {
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {
                    mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {}

                        @Override
                        public void onDone(String s) {
                            if(s.equals("input")) {
                                if (currEditText != null) {
                                    //read next label
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "label");
                                    mTTS.speak(text[i],QUEUE_ADD,map);
                                    //pause for 1 sec before speech starts
                                    try { Thread.sleep(1000); }
                                    catch (InterruptedException ex) { android.util.Log.d("new entry", ex.toString()); }
                                    askSpeechInput(currEditText);
                                }
                            }
                        }

                        @Override
                        public void onError(String s) {}
                    });
                    int result = mTTS.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                    else {}
                }
                else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private void initSTT() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,text[i]);
        i++;
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException a) {}
    }

    public void askSpeechInput(View view) {
        if(view == voiceInput) {
            i = 0;
            editPos = 0;
            currEditText = editTexts[0];
            editTexts[0].requestFocus();

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "label");
            mTTS.speak(text[i],QUEUE_ADD ,map);
            try { Thread.sleep(1000); }
            catch (InterruptedException ex) { android.util.Log.d("new entry", ex.toString()); }
        }
        initSTT();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //validate results
                    result.add(0, Integer.toString(editPos));
                    ValidateResults validate = new ValidateResults();
                    validate.execute(result);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "input");
                    mTTS.speak(currEditText.getText().toString(),QUEUE_ADD ,map);
                    currEditText = findViewById(currEditText.getNextFocusDownId());
                    if(currEditText != null) {
                        //not working in UtteranceProgressListener
                        currEditText.requestFocus();
                        editPos++;
                    }
                }
                break;
            }
        }
    }

    public void AddData(View view) throws ParseException {
        boolean isSuccessful = myDb.insertData(cow.cowNum, cow.dueCalveDate, cow.sireOfCalf, cow.calfBW, cow.calvingDate, cow.calvingDiff, cow.condition, cow.sex, cow.fate, cow.calfIndentNo, cow.remarks);
        if (isSuccessful)
            Toast.makeText(NewEntryActivity.this, "Data is inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NewEntryActivity.this, "Insertion failed", Toast.LENGTH_LONG).show();
    }

    private int id = 1;
    public void AddNewCalf(View view) {
        if(id > 3){
            Toast.makeText(NewEntryActivity.this,"Max of four twin calves allowed!",Toast.LENGTH_LONG).show();
            return;
        }
        EditText twinCalf = new EditText(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout1);
        twinCalf.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        twinCalf.setEms(10);
        twinCalf.setHint("Twin Calf ID");
        twinCalf.setId(id);
        int pos = 3 + id;
        id++;
        linearLayout.addView(twinCalf,pos);
    }

    private class ValidateResults extends AsyncTask<ArrayList<String>, Void, String> {
        int index = 0;

        @Override
        protected String doInBackground(ArrayList<String>... results) {
            String cleaned = "";
            try {
                URL request = new URL("https://api.wit.ai/message?v=20180802&q=" + results[0].get(1));
                HttpsURLConnection connection = (HttpsURLConnection) request.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer JL26524NMPCGBFR7253SR5K2DWKUHXUW");
                connection.connect();

                BufferedReader buffRead = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                String output = buffRead.readLine();
                if (output != null) {
                    cleaned = output;
                }
            } catch (MalformedURLException e) {
                System.out.print("url erro");
            } catch (IOException e) {
                System.out.print("IO error");
            }
            index = Integer.parseInt(results[0].get(0));
            return cleaned;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsob = new JSONObject(result);
                String textInput = jsob.getString("_text");
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                try {
                    JSONObject entities = jsob.getJSONObject("entities");
                    Iterator<String> keys = entities.keys();
                    JSONArray curr = entities.getJSONArray(keys.next());
                    entities = curr.getJSONObject(0);
                    textInput = entities.getString("value");
                } catch (Exception e) {
                    Log.e("SpeechTest",Log.getStackTraceString(e));
                }
                switch(index) {
                    case 0: cow.cowNum = Integer.parseInt(textInput); break;
                    case 1: cow.calfIndentNo = Integer.parseInt(textInput); break;
                    case 2: cow.dueCalveDate = formater.parse(textInput); break;
                    case 3: cow.sireOfCalf = Integer.parseInt(textInput); break;
                    case 4: cow.calfBW = Double.parseDouble(textInput); break;
                    case 5: cow.calvingDate = formater.parse(textInput); break;
                    case 6: cow.calvingDiff = textInput; break;
                    case 7: cow.condition = textInput; break;
                    case 8: cow.sex = textInput; break;
                    case 9: cow.fate = textInput; break;
                    case 10: cow.remarks = textInput; break;
                }
                editTexts[index].setText(textInput);
            } catch (Exception e) {
                Log.e("SpeechTest",Log.getStackTraceString(e));
                if(index==0){
                    askSpeechInput(voiceInput);
                    Toast.makeText(NewEntryActivity.this, "Invalid cow number", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}


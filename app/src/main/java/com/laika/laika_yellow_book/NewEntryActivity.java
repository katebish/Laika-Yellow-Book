package com.laika.laika_yellow_book;

import android.content.ActivityNotFoundException;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;
import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;


public class NewEntryActivity extends AppCompatActivity{
    private DbHelper myDb;
    private TextToSpeech mTTS;
    private EditText edit_CowNumber, edit_DueCalveDate, edit_SireIfCalf, edit_CalfBW, edit_CalvingDate, edit_CalvingDiff, edit_Condition, edit_Sex, edit_Fate, edit_CalfIndent,edit_Remarks;
    private Button voiceInput;
    private LinearLayout layout;
    private int childCount;
    private String[] text;
    private int i = 0;
    private EditText currEditText;

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
                text[c] = ((TextView) v).getText().toString();
                c++;
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
            currEditText = edit_CowNumber;
            edit_CowNumber.requestFocus();

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
                    currEditText.setText(result.get(0));
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "input");
                    mTTS.speak(currEditText.getText().toString(),QUEUE_ADD ,map);
                    currEditText = findViewById(currEditText.getNextFocusDownId());
                    if(currEditText != null) {
                        //not working in UtteranceProgressListener
                        currEditText.requestFocus();
                    }
                }
                break;
            }
        }
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
}

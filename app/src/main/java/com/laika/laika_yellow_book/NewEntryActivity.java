package com.laika.laika_yellow_book;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;


public class NewEntryActivity extends AppCompatActivity implements AsyncResponse {
    private DbHelper myDb;
    private TextToSpeech mTTS;
    private ImageButton voiceInput;
    private String[] labels;
    private int i = 0;
    private EditText currEditText;
    private EditText[] editTexts;
    private DataLine data;
    private boolean isIndividual = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        myDb = new DbHelper(this);
        data = new DataLine();

        editTexts = new EditText[11];
        editTexts[0] = (EditText) findViewById(R.id.edit_CowNumber);
        editTexts[1] = (EditText) findViewById(R.id.edit_CalfID);
        editTexts[2] = (EditText) findViewById(R.id.edit_DueCalveDate);
        editTexts[3] = (EditText) findViewById(R.id.edit_SireOfCalf);
        editTexts[4] = (EditText) findViewById(R.id.edit_CalfBW);
        editTexts[5] = (EditText) findViewById(R.id.edit_CalvingDate);
        editTexts[6] = (EditText) findViewById(R.id.edit_CalvingDiff);
        editTexts[7] = (EditText) findViewById(R.id.edit_Condition);
        editTexts[8] = (EditText) findViewById(R.id.edit_Sex);
        editTexts[9] = (EditText) findViewById(R.id.edit_Fate);
        editTexts[10] = (EditText) findViewById(R.id.edit_Remarks);

        voiceInput = (ImageButton) findViewById(R.id.btn_VoiceInput);

        final InputValidation inputValidation = new InputValidation();
        inputValidation.setData(data);

        int tag = 0;
        for (final EditText et : editTexts) {
            et.setTag(tag);
            tag++;
            et.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (motionEvent.getRawX() >= (et.getRight() - et.getCompoundDrawables()[2].getBounds().width())) {
                            isIndividual = true;
                            currEditText = (EditText) view;
                            currEditText.requestFocus();
                            if (currEditText.getTag() != null) {
                                i = (int) currEditText.getTag();
                            }
                            askSpeechInput(currEditText);
                            return true;
                        }
                    }
                    return false;
                }
            });

            //validate input
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        String input = et.getText().toString().trim();
                        if (!input.isEmpty()) {
                            int curr = -1;
                            if (view.getTag() != null)
                                curr = (int) et.getTag();
                            String err = inputValidation.validate(input,curr);
                            if(!err.isEmpty()){
                                //###################################
                                //if err not null, set error message
                                //###################################
                                currEditText.setBackgroundResource(R.drawable.edittext_error);
                            }
                        }
                    }
                    else
                        currEditText = (EditText) view;
                }
            });
        }

        //get all label values
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
        int childCount = layout.getChildCount();
        labels = new String[childCount];
        int c = 0;
        for (int i = 0; i < childCount; i++) {
            View v = layout.getChildAt(i);
            if(v instanceof TextInputLayout) {
                labels[c] = ((TextInputLayout) v).getHint().toString();
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

                            if (s.equals("input")) {
                                if (!isIndividual && currEditText != null) {
                                    //read next label
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "label");
                                    mTTS.speak(labels[i], QUEUE_ADD, map);
                                    //pause for 1 sec before speech starts
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException ex) {
                                        android.util.Log.d("new entry", ex.toString());
                                    }
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
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    private final int REQ_CODE_SPEECH_INPUT = 100;

    public void askSpeechInput(View view) {
        if (view == voiceInput) {
            i = 0;
            isIndividual = false;
            currEditText = editTexts[0];
            currEditText.requestFocus();

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "label");
            mTTS.speak(labels[i], QUEUE_ADD, map);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Log.e("new entry", ex.toString());
            }
        }
        initSTT();
    }

    private void initSTT() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, labels[i]);
        i++;
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Log.e("STT", "Initialization failed " + a.getMessage());
        }
    }

    //Call after valid result returns
    private void speakResult(View view) {
        currEditText = (EditText) view;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "input");
        mTTS.speak(currEditText.getText().toString(), QUEUE_ADD, map);
        if (!isIndividual) {
            //iterate through all textboxes
            currEditText = findViewById(currEditText.getNextFocusDownId());
            if (currEditText != null) {
                currEditText.requestFocus();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != intent) {
                    ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //check internet connection
                    try {
                        if(currEditText == editTexts[2]){
                            currEditText.setText(result.get(0));
                            currEditText.setEnabled(false);
                            ValidateResultsAPI validateResult = new ValidateResultsAPI();
                            validateResult.delegate = this;
                            validateResult.execute(result.get(0));
                        }
                        else if(currEditText == editTexts[5]) {
                            currEditText.setText(result.get(0));
                            currEditText.setEnabled(false);
                            ValidateResultsAPI validateResult = new ValidateResultsAPI();
                            validateResult.delegate = this;
                            validateResult.execute(result.get(0));
                        }
                        else {
                            currEditText.setText(result.get(0));
                            speakResult(currEditText);
                        }
                    }catch (Exception e) {
                        Log.e("STT", e.getMessage());
                    }
                }
                break;
            }
        }
    }

    public void AddData(View view) {
        //current textbox focus is unchanged,
        //but still triggers its listener for validation
        String input = currEditText.getText().toString();
        if (!input.isEmpty()) {
            final InputValidation inputValidation = new InputValidation();
            if (!input.isEmpty()) {
                int curr = -1;
                if (view.getTag() != null)
                    curr = (int) currEditText.getTag();
                String err = inputValidation.validate(input,curr);
                if(!err.isEmpty()){
                    //###################################
                    //if err not null, set error message
                    //###################################
                    currEditText.setBackgroundResource(R.drawable.edittext_error);
                }
            }
        }
        if(editTexts[0].getText().toString().isEmpty()){
            //##############################
            //sets error message
            //##############################
            editTexts[0].setBackgroundResource(R.drawable.edittext_error);
            editTexts[0].requestFocus();
            Toast.makeText(NewEntryActivity.this, "Error, Cow Number cannot be blank!", Toast.LENGTH_LONG).show();
            return;
        }
        else if(editTexts[5].getText().toString().isEmpty()) {
            //##############################
            //sets error message
            //##############################
            editTexts[5].setBackgroundResource(R.drawable.edittext_error);
            editTexts[0].requestFocus();
            Toast.makeText(NewEntryActivity.this, "Error, Calving Date cannot be blank!", Toast.LENGTH_LONG).show();
            return;
        }
        //##############################
        //also check if has invalid inputs
        //##############################
        boolean isSuccessful = myDb.insertData(data.cowNum, data.dueCalveDate, data.sireOfCalf, data.calfBW, data.calvingDate, data.calvingDiff, data.condition, data.sex, data.fate, data.calfIndentNo, data.remarks);
        if (isSuccessful)
            Toast.makeText(NewEntryActivity.this, "Data is inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NewEntryActivity.this, "Insertion failed", Toast.LENGTH_LONG).show();
    }

    private int id = 1;
    private int count = 1;
    public void AddNewCalf(View view) {
        if (count > 3) {
            Toast.makeText(NewEntryActivity.this, "Max of four twin calves allowed!", Toast.LENGTH_LONG).show();
            return;
        }
        final EditText twinCalf = new EditText(this);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        twinCalf.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        twinCalf.setEms(10);
        twinCalf.setHint("Twin Calf ID");
        twinCalf.setId(id);
        twinCalf.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.closs_button, 0);
        twinCalf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (twinCalf.getRight() - twinCalf.getCompoundDrawables()[2].getBounds().width())) {
                        // your action here
                        linearLayout.removeView(twinCalf);
                        count--;
                        return true;
                    }
                }
                return false;
            }
        });

        int pos = 3 + count;
        id++;
        count++;
        linearLayout.addView(twinCalf, pos);
    }

    @Override
    public void processFinish(final String output) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            data.dueCalveDate = format.parse(output);
            currEditText.setText(format.format(data.dueCalveDate));
            if(!currEditText.isEnabled())
                currEditText.setEnabled(true);
            speakResult(currEditText);
        } catch (ParseException e) {
            currEditText.setBackgroundResource(R.drawable.edittext_error);
            //###################################
            //set error message
            //errormessage = e.getMessage();
            //###################################
        }
    }
}
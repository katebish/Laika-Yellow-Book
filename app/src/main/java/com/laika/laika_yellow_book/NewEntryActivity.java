package com.laika.laika_yellow_book;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;


public class NewEntryActivity extends AppCompatActivity implements AsyncResponse {
    private DbHelper myDb;
    private TextToSpeech mTTS;
    private String[] labels;
    private int index = 0;
    private EditText currEditText;
    private EditText[] editTexts;
    private DataLine data;
    private boolean isIndividual = false;
    private TextInputLayout[] textInputLayout;
    private InputValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        myDb = new DbHelper(this);
        data = new DataLine();

        inputValidation = new InputValidation();
        inputValidation.setData(data);
        editTexts = new EditText[11];
        textInputLayout = new TextInputLayout[11];

        //get all label values
        LinearLayout layout = findViewById(R.id.linearLayout1);
        labels = new String[11];
        int c = 0;
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if(v instanceof TextInputLayout) {
                textInputLayout[c] = (TextInputLayout)v;
                editTexts[c] = ((TextInputLayout) v).getEditText();
                labels[c] = ((TextInputLayout) v).getHint().toString();
                c++;
            }
        }

        setDateTimePicker(editTexts[1],1);
        setDateTimePicker(editTexts[4],4);


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
                                index = (int) currEditText.getTag();
                            }
                            askSpeechInput(currEditText);
                            return true;
                        }
                    }
                    return false;
                }
            });

            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    //removes error message when field is blank
                    if(s.length() == 0) {
                        if(et.getTag()!=null) {
                            int i = (int) et.getTag();
                            textInputLayout[i].setError(null);
                        }
                    }
                }
            });

            //validate input
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        String input = et.getText().toString().trim();
                        if (!input.isEmpty()) {
                            int i = -1;
                            if (view.getTag() != null)
                                i = (int) et.getTag();
                            String err = inputValidation.validate(input,i);
                            if(!err.isEmpty()){
                                textInputLayout[i].setError(err);
                            }
                            else
                                textInputLayout[i].setError(null);
                        }
                    }
                    else {
                        EditText currFocus = (EditText) getCurrentFocus();
                        if(currFocus != null & currFocus != view)
                            currFocus.clearFocus();
                        currEditText = (EditText) view;
                    }
                }
            });
        }

        initTTS();
    }

    private void setDateTimePicker(final EditText ed, final int index) {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                ed.setText(format.format(myCalendar.getTime()));
                if(index == 1) {
                    data.dueCalveDate = myCalendar.getTime();
                    textInputLayout[index].setError(null);
                }
                else if(index == 4) {
                    data.calvingDate = myCalendar.getTime();
                    textInputLayout[index].setError(null);
                }
            }
        };

        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewEntryActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
                                    mTTS.speak(labels[index], QUEUE_ADD, map);
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
        if (view == findViewById(R.id.btn_VoiceInput)) {
            index = 0;
            isIndividual = false;
            currEditText = editTexts[0];
            currEditText.requestFocus();

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "label");
            mTTS.speak(labels[index], QUEUE_ADD, map);
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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, labels[index]);
        if(!isIndividual)
            index++;
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
            if(findViewById(currEditText.getNextFocusDownId()) != null){
                currEditText = findViewById(currEditText.getNextFocusDownId());
                currEditText.requestFocus();
            }
        }
    }

    private boolean hasInternet() {
        boolean hasInternet;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            hasInternet = true;
        }
        else
            hasInternet = false;
        return hasInternet;
    }

    private boolean isKeyword (View view, String text) {
        currEditText = (EditText) view;
        String keyword = text.trim();
        String[] keywords = keyword.split(" ");
        if(keywords.length == 1) {
            switch (keyword) {
                case "skip":
                    if(findViewById(currEditText.getNextFocusDownId()) != null){
                        currEditText = findViewById(currEditText.getNextFocusDownId());
                        currEditText.requestFocus();
                        if (!isIndividual && currEditText != null) {
                            //read next label
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "label");
                            mTTS.speak(labels[index], QUEUE_ADD, map);
                            //pause for 1 sec before speech starts
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                android.util.Log.d("new entry", ex.toString());
                            }
                            askSpeechInput(currEditText);
                        }
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != intent) {
                    ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //check keywords
                    boolean isKeyword = isKeyword(currEditText, result.get(0));

                    if(!isKeyword) {
                        //check internet connection
                        try {
                            if (currEditText == editTexts[1]) {
                                boolean hasInternet = hasInternet();
                                if (hasInternet) {
                                    currEditText.setText(result.get(0));
                                    currEditText.setEnabled(false);
                                    ValidateResultsAPI validateResult = new ValidateResultsAPI();
                                    validateResult.delegate = this;
                                    validateResult.execute(result.get(0));
                                } else {
                                    Date date = inputValidation.parseDate(result.get(0));
                                    if (date == null) {
                                        textInputLayout[1].setError("Format: first of August 2018");
                                        currEditText.setText(result.get(0));
                                    } else {
                                        data.dueCalveDate = date;
                                        currEditText.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                                    }
                                    speakResult(currEditText);
                                }
                            } else if (currEditText == editTexts[4]) {
                                boolean hasInternet = hasInternet();
                                if (hasInternet) {
                                    currEditText.setText(result.get(0));
                                    currEditText.setEnabled(false);
                                    ValidateResultsAPI validateResult = new ValidateResultsAPI();
                                    validateResult.delegate = this;
                                    validateResult.execute(result.get(0));
                                } else {
                                    Date date = inputValidation.parseDate(result.get(0));
                                    if (date == null) {
                                        textInputLayout[4].setError("Format: first of August 2018");
                                        currEditText.setText(result.get(0));
                                    } else {
                                        data.calvingDate = date;
                                        currEditText.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                                    }
                                    speakResult(currEditText);
                                }
                            } else {
                                currEditText.setText(result.get(0));
                                speakResult(currEditText);
                            }
                        } catch (Exception e) {
                            Log.e("STT", e.getMessage());
                        }
                    }
                }
            }
            break;
        }
    }

    public void AddData(View view) {
        if(editTexts[0].getText().toString().isEmpty()){
            textInputLayout[0].setError("Cow number cannot be blank!");
            editTexts[0].requestFocus();
            Toast.makeText(NewEntryActivity.this, "Error, Cow Number cannot be blank!", Toast.LENGTH_LONG).show();
            return;
        }
        else if(editTexts[4].getText().toString().isEmpty()) {
            textInputLayout[4].setError("Calving date cannot be blank!");
            editTexts[4].requestFocus();
            Toast.makeText(NewEntryActivity.this, "Error, Calving Date cannot be blank!", Toast.LENGTH_LONG).show();
            return;
        }


        String input = currEditText.getText().toString();
        if (!input.isEmpty()) {
            currEditText.getOnFocusChangeListener().onFocusChange(currEditText,false);
        }

        //check if has invalid input
        for (TextInputLayout layout: textInputLayout) {
            if(!TextUtils.isEmpty(layout.getError())) {
                layout.getEditText().requestFocus();
                Toast.makeText(NewEntryActivity.this, "Please make sure that all fields are valid!", Toast.LENGTH_LONG).show();
                return;
            }
        }
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
        int i = -1;
        if(currEditText.getTag()!=null)
            i = (int) currEditText.getTag();
        try {
            data.dueCalveDate = format.parse(output);
            currEditText.setText(format.format(data.dueCalveDate));
            if(!currEditText.isEnabled())
                currEditText.setEnabled(true);
            textInputLayout[i].setError(null);
            speakResult(currEditText);
        } catch (ParseException e) {
           textInputLayout[i].setError("Invalid date, please try again.");
        }
    }
}
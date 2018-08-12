package com.laika.laika_yellow_book;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class SpeechToDate {
    String stringToConvert;
    Date date;
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Date parseDate(String stringToConvert, boolean hasInternet) throws InterruptedException, ExecutionException {
        this.stringToConvert = stringToConvert;
        if(hasInternet) {
            ValidateResultsAPI validate = new ValidateResultsAPI();
            Object result = validate.execute(stringToConvert).get();
            validate.wait();
        }
        else {
            //call validation method
        }
        return date;
    }

    public class ValidateResultsAPI extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            String cleaned = "";
            try {
                URL request = new URL("https://api.wit.ai/message?v=20180802&q=" + (String) objects[0]);
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
                Log.e("cURL", Log.getStackTraceString(e));
            } catch (IOException e) {
                Log.e("cURL", Log.getStackTraceString(e));
            }
            return cleaned;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsob = new JSONObject(result);
                String _date = jsob.getString("_text");
                try {
                    JSONObject entities = jsob.getJSONObject("entities");
                    Iterator<String> keys = entities.keys();
                    JSONArray curr = entities.getJSONArray(keys.next());
                    entities = curr.getJSONObject(0);
                    _date = entities.getString("value");
                    date = format.parse(_date);
                } catch (Exception e) {
                    Log.e("cURL", Log.getStackTraceString(e));
                }
            } catch (Exception e) {
                Log.e("cURL", Log.getStackTraceString(e));
            }
        }
    }
}

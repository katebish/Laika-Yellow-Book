package com.laika.laika_yellow_book;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class ValidateResultsAPI extends AsyncTask<Object, Void, String> {
    public AsyncResponse delegate = null;

    @Override
    protected String doInBackground(Object... objects) {
        String cleaned = "";
        try {
            String requestString = Uri.encode((String) objects[0]);
            URL request = new URL("https://api.wit.ai/message?v=20180802&q=" + requestString);
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
        String _date = "";
        try {
            JSONObject jsob = new JSONObject(result);
            _date = jsob.getString("_text");
            try {
                JSONObject entities = jsob.getJSONObject("entities");
                Iterator<String> keys = entities.keys();
                JSONArray curr = entities.getJSONArray(keys.next());
                entities = curr.getJSONObject(0);
                _date = entities.getString("value");
            } catch (Exception e) {
                Log.e("cURL", Log.getStackTraceString(e));
            }
        } catch (Exception e) {
            Log.e("cURL", Log.getStackTraceString(e));
        }
        delegate.processFinish(_date);
    }
}
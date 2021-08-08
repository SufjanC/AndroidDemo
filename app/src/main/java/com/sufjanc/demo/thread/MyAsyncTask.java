package com.sufjanc.demo.thread;

import android.database.CursorJoiner;
import android.os.AsyncTask;
import android.text.PrecomputedText;

/*
    TODO: AsyncTask练习
 */
public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(String... strings) {
        publishProgress();
        return null;
    }
}

package com.example.dara.popularmovies.utilities;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

public class MoviesAsyncTask extends AsyncTask<URL, Void, String> {

    private final WeakReference<ProgressBar> mProgressBar;

    private final OnTaskCompleted mTaskCompleted;

    //Constructor
    public MoviesAsyncTask(OnTaskCompleted mTaskCompleted, ProgressBar progressBar) {
        this.mTaskCompleted = mTaskCompleted;
        this.mProgressBar = new WeakReference<>(progressBar);
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressBar.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL url = urls[0];
        Log.d(">>>Param", url.toString());
        String jsonResponse = null;
        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            Log.d(">>>>", "RESPONSE" + jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String result) {
        mTaskCompleted.onTaskCompleted(result);
    }


}

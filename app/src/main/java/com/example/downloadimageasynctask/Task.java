package com.example.downloadimageasynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Task extends AsyncTask<String, Integer, Void> {
    int count = 0;
    int contentl = -1;

    @Override
    protected void onPostExecute(Void aVoid) {
        // linearLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPreExecute() {
        //linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = (int) (((double) values[0] / contentl) * 100);
        //   progressBar.setProgress(progress);
    }

    @Override
    protected Void doInBackground(String... voids) {

        String imgurl = voids[0];
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            URL url = new URL(imgurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            contentl = httpURLConnection.getContentLength();
            inputStream = httpURLConnection.getInputStream();
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + Uri.parse(imgurl).getLastPathSegment());
            Log.d("msg", file.getAbsolutePath());
            fileOutputStream = new FileOutputStream(file);
            int read = -1;
            byte b[] = new byte[1024];
            while ((read = inputStream.read(b)) != -1) {

                fileOutputStream.write(b, 0, read);
                count = count + read;
                publishProgress(count);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.d("msg", e.getMessage());
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.d("msg", e.getMessage());
                }
            }
        }
        return null;
    }
}


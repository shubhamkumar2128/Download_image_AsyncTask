package com.example.downloadimageasynctask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button dwnld;
    EditText editText;
    LinearLayout linearLayout;
    ListView listView;
    String links[];
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 121);

        }
        links = getResources().getStringArray(R.array.links);
        linearLayout = findViewById(R.id.linearlay);
        dwnld = findViewById(R.id.downloadbtn);
        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);
        editText = findViewById(R.id.eturl);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, links));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(links[position]);
            }
        });
        dwnld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Task().execute(editText.getText().toString());
            }
        });
    }


    class Task extends AsyncTask<String, Integer, Void> {
        int count = 0;
        int contentl=-1;

        @Override
        protected void onPostExecute(Void aVoid) {
            linearLayout.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress=(int)(((double)values[0]/contentl)*100);
            progressBar.setProgress(progress);
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
                contentl=httpURLConnection.getContentLength();
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
}




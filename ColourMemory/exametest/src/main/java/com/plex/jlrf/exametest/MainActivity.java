package com.plex.jlrf.exametest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private ListView lvURLs;
    private TextView mTextView;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mTextView = (TextView) this.findViewById(R.id.tv_test);

        lvURLs = (ListView) this.findViewById(R.id.lv_urls);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lvURLs.setAdapter(adapter);
        lvURLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = adapter.getItem(position);
                Intent intentURL = new Intent(Intent.ACTION_VIEW);
                intentURL.setData(Uri.parse(url));
                startActivity(intentURL);
            }
        });
    }


    private void exampleAsyncTask() {
        AsyncTask asynk = new AsyncTask() {

            @Override
            protected void onProgressUpdate(Object[] values) {
                super.onProgressUpdate(values);
                mTextView.setText(String.valueOf(values[0]));
            }

            @Override
            protected Object doInBackground(Object[] params) {
                for (int i = 10; i-- >= 0; ) {
                    try {
                        Thread.sleep(1000);
                        publishProgress(i);

                    } catch (Exception e) {
                    }
                }
                return null;

            }
        };

        asynk.execute();
    }

    String filename = "myfile";
    ArrayAdapter<String> adapter;

    private void exampleCreateFile() {
        String[] arrURLs = getResources().getStringArray(R.array.arr_urls);
        StringBuilder builder = new StringBuilder();
        for (String s : arrURLs) {
            builder.append(s + "\n");
        }
        File file = new File(mContext.getFilesDir(), filename);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(builder.toString().getBytes());
            outputStream.close();
            Toast.makeText(mContext, "Success File Created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void exampleReadFile() {
        String yourFilePath = mContext.getFilesDir() + "/" + filename;
        File myFile = new File(yourFilePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                adapter.add(line);
            }
            adapter.notifyDataSetChanged();
            Toast.makeText(mContext, "Success File Created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_file) {
            exampleCreateFile();
            return true;
        }
        if (id == R.id.action_async_task) {
            exampleAsyncTask();
            return true;
        }
        if (id == R.id.action_read_file) {
            exampleReadFile();
        }

        return super.onOptionsItemSelected(item);
    }
}

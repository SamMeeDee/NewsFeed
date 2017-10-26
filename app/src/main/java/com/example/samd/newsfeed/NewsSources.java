package com.example.samd.newsfeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsSources extends AppCompatActivity {

    ArrayList<Source> sourcesList = new ArrayList<>();
    boolean sourcesDL = false;
    static String SOURCE_KEY="SOURCE";
    static String NAME_KEY="NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_sources);

        new getSourcesAsyc().execute("https://newsapi.org/v1/sources");

        ListView listView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<Source> adapter =
                new ArrayAdapter<Source>(this, android.R.layout.simple_list_item_1,
                        android.R.id.text1, sourcesList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("demo", "Clicked item "+ sourcesList.get(position).toString());
                Intent i = new Intent(getApplicationContext(), ArticleList.class);
                i.putExtra(SOURCE_KEY, sourcesList.get(position).id);
                i.putExtra(NAME_KEY, sourcesList.get(position).name);
                startActivity(i);
            }
        });
    }

    private class getSourcesAsyc extends AsyncTask<String, Integer, String>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NewsSources.this);
            progressDialog.setMessage("Getting Sources..");
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("demo","onProgressUpdate progress is " + values[0]);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpURLConnection connection = null;
            JSONObject root;
            JSONArray sourcesAr;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    root = new JSONObject(json);
                    sourcesAr = root.getJSONArray("sources");

                    progressDialog.setMax(sourcesAr.length());
                    for (int i=0; i<sourcesAr.length(); i++) {
                        JSONObject sourceObj = sourcesAr.getJSONObject(i);
                        Source source = new Source();
                        source.id = sourceObj.getString("id");
                        source.name = sourceObj.getString("name");
                        sourcesList.add(source);
                        publishProgress(i);
                    }

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return "Sources Populated";
        }

        @Override
        protected void onPostExecute(String string) {
            Log.d("demo", string);
            progressDialog.dismiss();
        }
    }
}

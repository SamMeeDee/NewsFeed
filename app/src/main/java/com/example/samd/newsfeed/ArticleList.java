package com.example.samd.newsfeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ArticleList extends AppCompatActivity {

    ArrayList<Article> articleList = new ArrayList<>();
    boolean articlesDL=true;
    String url;
    static String URL_KEY="URL";
    static String PAGE_NAME_KEY="PAGE NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        if(getIntent() != null && getIntent().getExtras() != null){
            url="https://newsapi.org/v1/articles?source="+
                    getIntent().getExtras().getString(NewsSources.SOURCE_KEY)+
                    "&apiKey=72d22a50ad03475ab772262917f76fb6";
        }

        new getArticlesAsync().execute(url);

        ListView listView = (ListView)findViewById(R.id.listView);
        ArticleAdapter adapter = new ArticleAdapter(this, R.layout.article_item, articleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("demo", "Clicked item "+ articleList.get(position).toString());
                Intent i = new Intent(getApplicationContext(), WebViewActivity.class);
                i.putExtra(URL_KEY, articleList.get(position).url);
                i.putExtra(PAGE_NAME_KEY, articleList.get(position).title);
                startActivity(i);
            }
        });
        setTitle(getIntent().getExtras().getString(NewsSources.NAME_KEY));
    }

    private class getArticlesAsync extends AsyncTask<String, Integer, String>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ArticleList.this);
            progressDialog.setMessage("Getting Articles..");
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
        protected void onPostExecute(String s) {
            Log.d("demo", s);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            JSONObject root;
            JSONArray articlesAr;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    root = new JSONObject(json);
                    articlesAr = root.getJSONArray("articles");

                    progressDialog.setMax(articlesAr.length());
                    for (int i=0; i<articlesAr.length(); i++) {
                        JSONObject sourceObj = articlesAr.getJSONObject(i);
                        Article article =new Article();
                        article.title = sourceObj.getString("title");
                        article.author = sourceObj.getString("author");
                        article.url = sourceObj.getString("url");
                        article.urlToImage = sourceObj.getString("urlToImage");
                        article.publishedAt = sourceObj.getString("publishedAt");
                        articleList.add(article);
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
            return "Articles Populated";
        }
    }

}

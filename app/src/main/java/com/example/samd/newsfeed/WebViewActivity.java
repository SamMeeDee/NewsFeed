package com.example.samd.newsfeed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        if(getIntent() != null && getIntent().getExtras() != null){
            url = getIntent().getExtras().getString(ArticleList.URL_KEY);
            WebView webView=(WebView)findViewById(R.id.web_view);
            setTitle(getIntent().getExtras().getString(ArticleList.PAGE_NAME_KEY));
            webView.loadUrl(url);
        }
    }
}

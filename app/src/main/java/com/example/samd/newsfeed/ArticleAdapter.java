package com.example.samd.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by samd on 10/25/2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article>
{
    public ArticleAdapter(Context context, int resource, List<Article> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.article_item, parent, false);

        ImageView imageViewArticleImg = (ImageView)convertView.findViewById(R.id.articleImage);
        TextView textViewAuthor = (TextView)convertView.findViewById(R.id.author);
        TextView textViewTitle = (TextView)convertView.findViewById(R.id.title);
        TextView textViewDate = (TextView)convertView.findViewById(R.id.date);

        Picasso.with(getContext()).load(article.urlToImage).resize(50,50).centerCrop().into(imageViewArticleImg);
        textViewAuthor.setText(article.author);
        textViewTitle.setText(article.title);
        textViewDate.setText(article.publishedAt);

        return convertView;
    }

}

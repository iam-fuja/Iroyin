package com.example.hp.iroyin;

import android.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<News> {

    public static final String TAG = MainActivity.class.getName();
    private static final int LOADER_ID = 1;
    private static final String QUERY_URL = "https://content.guardianapis.com/search?api-key=80279424-7842-44f3-9fd8-3e523024ffe6";

    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "TEST: MainActivity onCreate() called ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoaderManager loaderManager = getLoaderManager();

        Log.i(TAG, "TEST: calling initLoader()... ");
        loaderManager.initLoader(LOADER_ID, null, this);
        ListView newsListView = findViewById(R.id.list);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(adapter);


        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News trendingNews = (News) adapter.getItem(position);
                Uri newsUri = Uri.parse(trendingNews.getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(intent);
            }
        });
      }

    @Override
    public android.content.Loader<News> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "TEST: onCreateLoader() called ... ");
        return new android.content.Loader<News>(this, QUERY_URL );
    //    return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<News> loader, News data) {
        Log.i(TAG, "TEST: onLoadFinished() called ... ");
        adapter.clear();

        if (!(freshnews == null && news.isEmpty())) {
            adapter.addAll();
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<News> loader) {
        Log.i(TAG, "TEST: onLoaderReset() called ... ");
        adapter.clear();
    }

}

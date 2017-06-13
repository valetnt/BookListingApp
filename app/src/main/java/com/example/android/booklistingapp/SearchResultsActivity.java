package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = SearchResultsActivity.class.getSimpleName();

    private String mQuery;
    private ProgressBar mProgressBar;
    private ViewGroup mEmptyView;
    private ListView mListView;
    private TextView mEmptyStateMessage;
    private TextView mEmptyStateHint;
    private Button mNewSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        mQuery = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=100";

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mEmptyView = (ViewGroup) findViewById(R.id.empty_view);
        mEmptyStateHint = (TextView) findViewById(R.id.empty_list_search_hint);
        mEmptyStateMessage = (TextView) findViewById(R.id.empty_list_message);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setEmptyView(mEmptyView);
        mNewSearchButton = (Button) findViewById(R.id.new_search_button);

        mNewSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackToSearchForm = new Intent(SearchResultsActivity.this,
                        MainActivity.class);
                startActivity(goBackToSearchForm);
            }
        });

        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mNewSearchButton.setVisibility(View.GONE);

        // Check internet connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());

        if (isConnected) {
            // Get a LoaderManager to handle an AsyncTask
            LoaderManager loaderManager = getLoaderManager();
            // Launch the LoaderManager
            loaderManager.initLoader(0, null, this);

        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyStateMessage.setText(getString(R.string.no_internet_connection));
            mEmptyStateHint.setText(getString(R.string.no_internet_connection_hint));
        }
    }

    /*
     * Implementation of the LoaderCallbacks interface:
     */
    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        // Return a new instance of the loader BookListLoader
        return new BookListLoader(this, mQuery);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        // Update the UI according to the data returned by BookListLoader

        mProgressBar.setVisibility(View.GONE);

        if (data == null || data.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyStateMessage.setText(getString(R.string.no_results_found));
            mEmptyStateHint.setText(getString(R.string.no_results_found_hint));
            mNewSearchButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // When the loader is no longer needed, discard data
    }
}

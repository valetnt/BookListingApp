package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private String mQuery;
    private ProgressBar mProgressBar;
    private ViewGroup mEmptyView;
    private ListView mListView;
    private Button mNewSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        mQuery = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=100";

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mEmptyView = (ViewGroup) findViewById(R.id.empty_view);
        TextView hint = (TextView) findViewById(R.id.empty_list_search_hint);
        TextView emptyListMessage = (TextView) findViewById(R.id.empty_list_message);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setEmptyView(mEmptyView);
        mNewSearchButton = (Button) findViewById(R.id.new_search_button);

        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mNewSearchButton.setVisibility(View.GONE);

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
            emptyListMessage.setText(getString(R.string.no_internet_connection));
            hint.setText(getString(R.string.no_internet_connection_hint));
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

        if (data == null || data.get(0) == null) {
            mEmptyView.setVisibility(View.VISIBLE);
            mNewSearchButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // When the loader is no longer needed, discard data
    }
}

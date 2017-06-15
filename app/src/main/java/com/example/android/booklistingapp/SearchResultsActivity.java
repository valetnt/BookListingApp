package com.example.android.booklistingapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = SearchResultsActivity.class.getSimpleName();
    private static final String QUERY = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    private ProgressBar mProgressBar;
    private ViewGroup mEmptyView;
    private ListView mListView;
    private CustomAdapter mAdapter;
    private TextView mEmptyStateMessage;
    private TextView mEmptyStateHint;
    private Button mNewSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

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

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new CustomAdapter(this, new ArrayList<Book>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mListView.setAdapter(mAdapter);

        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mNewSearchButton.setVisibility(View.GONE);

        // Check internet connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());

        if (isConnected) {
            // If the device is connected to the network, we can perform an http request.
            // Get a LoaderManager to handle an AsyncTask on a background thread, in order to manage
            // the http request and properly receive the results to our API query.
            LoaderManager loaderManager = getSupportLoaderManager();
            // Launch the LoaderManager with an arbitrary index (we only use one loader)
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
        return new BookListLoader(this, QUERY);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {

        // As soon as the loader has finished loading results, hide the progress bar
        mProgressBar.setVisibility(View.GONE);

        // Clear the adapter of previous books data
        mAdapter.clear();

        // If query url was malformed, or if http request did not succeed, or if query did not
        // produce any results, show an empty page with a message saying that no result was found.
        if (data == null || data.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyStateMessage.setText(getString(R.string.no_results_found));
            // Show a hint on how to improve query
            mEmptyStateHint.setText(getString(R.string.no_results_found_hint));
            // Give the user the chance to try again with a new search
            mNewSearchButton.setVisibility(View.VISIBLE);

        } else {
            mListView.setVisibility(View.VISIBLE);
            mNewSearchButton.setVisibility(View.VISIBLE);
            // Update the UI according to the data returned by BookListLoader
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // When the loader is no longer needed, clear data
        mAdapter.clear();
    }
}

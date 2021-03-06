package com.example.android.booklistingapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklistingapp.MainActivity.QUERY;

public class SearchResultsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = SearchResultsActivity.class.getSimpleName();
    private static final String QUERY_START = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String QUERY_END = "&maxResults=40&langRestrict=en";

    private ProgressBar mProgressBar;
    private ViewGroup mEmptyView;
    private ListView mListView;
    private ImageView mEmptyStateImage;
    private CustomAdapter mAdapter;
    private TextView mEmptyStateMessage;
    private TextView mEmptyStateHint;
    private FrameLayout mNewSearchButton;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Receive the intent from MainActivity with the associated query string
        mQuery = getIntent().getStringExtra(QUERY);
        // Attach start and end pieces to the query string
        mQuery = QUERY_START + mQuery + QUERY_END;

        Log.i(LOG_TAG, mQuery);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mEmptyView = (ViewGroup) findViewById(R.id.empty_view);
        mEmptyStateImage = (ImageView) findViewById(R.id.empty_list_image);
        mEmptyStateHint = (TextView) findViewById(R.id.empty_list_search_hint);
        mEmptyStateMessage = (TextView) findViewById(R.id.empty_list_message);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setEmptyView(mEmptyView);
        mNewSearchButton = (FrameLayout) findViewById(R.id.new_search);

        mNewSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackToSearchForm = new Intent(SearchResultsActivity.this,
                        MainActivity.class);
                startActivity(goBackToSearchForm);
            }
        });

        /**
         * Create a new adapter that takes an empty list of {@link Book} objects as input
         */
        mAdapter = new CustomAdapter(this, new ArrayList<Book>());
        // Set the adapter on the ListView so the list can be populated in the user interface
        mListView.setAdapter(mAdapter);
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to visualize the selected book related page on books.google.com
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book selectedBook = mAdapter.getItem(position);
                Intent openWebPage = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(selectedBook.getLink()));
                startActivity(openWebPage);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mNewSearchButton.setVisibility(View.GONE);
        /*
         * CHECK INTERNET CONNECTION:
         *
         * Make this check onResume(), rather than onCreate(), since the loader is restarted
         * every time the activity is resumed.
         * In this way, if the activity is resumed with internet connection off,
         * the empty message that the user is going to receive is "you are offline",
         * rather than "no results found", which is more consistent with what the user expects.
         *
         * The price to pay for making this check onResume(), rather than onCreate(),
         * is that we are going to invoke initLoader every time the activity is resumed.
         * Therefore we are going to issue more server queries.
         */
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());

        if (isConnected) {
            /*
             * If the device is connected to the network, we can perform an http request.
             * Get a LoaderManager in order to be able to create and manage loaders.
             * First argument of method initLoader is arbitrary
             * (we don't care since we only use one loader).
             */
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, null, this);

        } else {
            // If the device is offline and a loader already exists,
            // then intercept it before it starts loading data and kill it.
            // Thus the message displayed will be "you are offline",
            // rather than "no results found".
            if (getSupportLoaderManager().getLoader(0) != null) {
                getSupportLoaderManager().destroyLoader(0);
            }
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyStateMessage.setText(getString(R.string.no_internet_connection));
            mEmptyStateHint.setText(getString(R.string.no_internet_connection_hint));
            mEmptyStateImage.setImageResource(R.drawable.no_wifi);
        }
    }

    /**
     * Implementation of the {@link LoaderManager.LoaderCallbacks} interface:
     */
    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        /**
         * Return a new instance of {@link BookListLoader}
         */
        return new BookListLoader(this, mQuery);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {

        // As soon as the loader has finished loading results, hide the progress bar
        mProgressBar.setVisibility(View.GONE);

        // Clear the adapter of previous books data
        mAdapter.clear();

        if (data == null || data.isEmpty()) {
            // If query did not produce any results, show an empty page with a message saying
            // that no result was found.
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyStateMessage.setText(getString(R.string.no_results_found));
            mEmptyStateImage.setImageResource(R.drawable.book);
            // Show a hint on how to improve query
            mEmptyStateHint.setText(getString(R.string.no_results_found_hint));
            // Give the user the chance to try again with a new search
            mNewSearchButton.setVisibility(View.VISIBLE);

        } else {
            mListView.setVisibility(View.VISIBLE);
            mNewSearchButton.setVisibility(View.VISIBLE);
            // If there is a valid list of books, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // When the loader is no longer needed, clear data
        mAdapter.clear();
    }
}

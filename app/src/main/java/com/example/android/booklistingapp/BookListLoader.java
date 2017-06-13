package com.example.android.booklistingapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class BookListLoader extends AsyncTaskLoader<List<Book>> {

    private String mQuery;

    public BookListLoader(Context context, String query) {
        super(context);
        mQuery = query;
    }

    @Override
    public List<Book> loadInBackground() {

        // Create an empty ArrayList of books
        ArrayList<Book> books = new ArrayList<Book>();

        return books;
    }


}

package com.example.android.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String QUERY = "query to be searched";

    private String mQuery = "";
    private EditText mEditText0;
    private EditText mEditText1;
    private EditText mEditText2;
    private String mField0 = "";
    private String mField1 = "";
    private String mField2 = "";
    private String mOption1;
    private String mOption2;
    private String mOrderBy;

    // Static method that turns a string containing spaces like "__hello_world_" into "hello+world"
    private static String concatenateText(String text) {
        if (text.contains(" ")) {
            // Trim empty spaces at the beginning of the String, so that
            // when the string is split we are sure that the first element of the array
            // is NOT an empty String
            String trimmed = text.trim();
            String[] split = trimmed.split(" ", 0);
            // Concatenate text recursively
            String concatenated = split[0];
            if (split.length > 1) {
                for (int i = 1; i < split.length; i++) {
                    if (!split[i].isEmpty()) {
                        concatenated += ("+" + split[i]);
                    }
                }
            }
            // Replace text with concatenated text
            text = concatenated;
        }
        return text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText0 = (EditText) findViewById(R.id.key0);
        mEditText1 = (EditText) findViewById(R.id.key1);
        mEditText2 = (EditText) findViewById(R.id.key2);
        RadioGroup radio1 = (RadioGroup) findViewById(R.id.radio1);
        RadioGroup radio2 = (RadioGroup) findViewById(R.id.radio2);
        RadioGroup radio3 = (RadioGroup) findViewById(R.id.radio3);
        // Initialize state of the radio buttons
        radio1.check(R.id.radio1_author);
        radio2.check(R.id.radio2_author);
        radio3.check(R.id.radio3_relevance);
        // Initialize search options accordingly
        mOption1 = "inauthor:";
        mOption2 = "inauthor:";
        mOrderBy = "&orderBy=relevance";

        radio1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio1_author) {
                    mOption1 = "inauthor:";
                } else if (checkedId == R.id.radio1_title) {
                    mOption1 = "intitle:";
                }
            }
        });

        radio2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio2_author) {
                    mOption2 = "inauthor:";
                } else if (checkedId == R.id.radio2_title) {
                    mOption2 = "intitle:";
                }
            }
        });

        radio3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio3_relevance) {
                    mOrderBy = "&orderBy=relevance";
                } else if (checkedId == R.id.radio3_date) {
                    mOrderBy = "&orderBy=newest";
                }
            }
        });

        FrameLayout searchButton = (FrameLayout) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check that at least one field is filled
                if (mEditText0.getText().toString().isEmpty() &&
                        mEditText1.getText().toString().isEmpty()
                        && mEditText2.getText().toString().isEmpty()) {
                    Toast.makeText(v.getContext(), "Fill at least one of the fields",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Finalize query string, register all the changes that have taken place.
                    // Then perform the search by sending an Intent to SearchResultsActivity.
                    // First field
                    mField0 = concatenateText(mEditText0.getText().toString());
                    mQuery = mField0;
                    // Second field
                    if (!(mEditText1.getText().toString().isEmpty())) {
                        mField1 = concatenateText(mEditText1.getText().toString());
                        if (!mQuery.isEmpty()) {
                            mQuery = mQuery + "+" + mOption1 + mField1;
                        } else {
                            mQuery = mOption1 + mField1;
                        }
                    }
                    // Third field
                    if (!(mEditText2.getText().toString().isEmpty())) {
                        mField2 = concatenateText(mEditText2.getText().toString());
                        if (!mQuery.isEmpty()) {
                            mQuery = mQuery + "+" + mOption2 + mField2;
                        } else {
                            mQuery = mOption2 + mField2;
                        }
                    }
                    // Include sorting preference
                    mQuery = mQuery + mOrderBy;
                    // Send an intent to SearchResultsActivity to perform a search
                    // with the search query attached to the intent as extra
                    Intent querySearch = new Intent(MainActivity.this, SearchResultsActivity.class);
                    querySearch.putExtra(QUERY, mQuery);
                    startActivity(querySearch);
                }
            }
        });
    }
}

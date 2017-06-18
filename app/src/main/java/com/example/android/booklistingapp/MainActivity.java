package com.example.android.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

/*
 *
 *
 *
 *
 *
 * TODO: FIX PERSISTENCE OF SPINNERS ON DEVICE ROTATION!!!!!!!!!!!!!!!!!!!!!!!!
 *
 *
 *
 *
 *
 *
 */

public class MainActivity extends AppCompatActivity {

    public static final String QUERY = "query to be searched";

    private String mQuery = "";
    private EditText mEditText0;
    private EditText mEditText1;
    private EditText mEditText2;
    private String mField0 = "";
    private String mField1 = "";
    private String mField2 = "";
    private Spinner mSpinner1;
    private Spinner mSpinner2;
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
        // Initially disable editable TextViews
        mEditText1.setEnabled(false);
        mEditText1.setFocusable(false);
        mEditText2.setEnabled(false);
        mEditText2.setFocusable(false);
        // Initially set spinners as invisible
        mSpinner1 = (Spinner) findViewById(R.id.spinner1);
        mSpinner2 = (Spinner) findViewById(R.id.spinner2);
        mSpinner1.setVisibility(View.GONE);
        mSpinner2.setVisibility(View.GONE);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinners
        mSpinner1.setAdapter(adapter);
        mSpinner2.setAdapter(adapter);

        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Enable editable TextView
                mEditText1.setEnabled(true);
                mEditText1.setFocusableInTouchMode(true);
                // Update mOption1
                switch (position) {
                    case 0:
                        // If TITLE has been selected
                        mOption1 = "intitle:";
                        break;
                    case 1:
                        // If AUTHOR has been selected
                        mOption1 = "inauthor:";
                        break;
                    case 2:
                        // If PUBLISHER has been selected
                        mOption1 = "inpublisher:";
                        break;
                    case 3:
                        // If SUBJECT has been selected
                        mOption1 = "insubject:";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Enable editable TextView
                mEditText2.setEnabled(true);
                mEditText2.setFocusableInTouchMode(true);
                // Update mOption2
                switch (position) {
                    case 0:
                        // If TITLE has been selected
                        mOption2 = "intitle:";
                        break;
                    case 1:
                        // If AUTHOR has been selected
                        mOption2 = "inauthor:";
                        break;
                    case 2:
                        // If PUBLISHER has been selected
                        mOption2 = "inpublisher:";
                        break;
                    case 3:
                        // If SUBJECT has been selected
                        mOption2 = "insubject:";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Display the spinner prompt views
        View prompt1 = findViewById(R.id.prompt1);
        View prompt2 = findViewById(R.id.prompt2);
        // As soon as the user clicks on the prompt, hide the prompt and show the spinner
        prompt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mSpinner1.setVisibility(View.VISIBLE);
            }
        });
        prompt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mSpinner2.setVisibility(View.VISIBLE);
            }
        });


        RadioGroup radio = (RadioGroup) findViewById(R.id.radio_group);
        // Initialize state of the radio group
        radio.check(R.id.radio_relevance);
        mOrderBy = "&orderBy=relevance";

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_relevance) {
                    mOrderBy = "&orderBy=relevance";
                } else if (checkedId == R.id.radio_date) {
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

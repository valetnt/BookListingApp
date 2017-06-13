package com.example.android.booklistingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         *
         * TODO: Support persistency of data while rotating the device!!!
         *
         */

        FrameLayout searchButton = (FrameLayout) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent querySearch = new Intent(MainActivity.this, SearchResultsActivity.class);
                startActivity(querySearch);
            }
        });
    }
}

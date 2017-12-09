package com.application.medicare.medicare.medicine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.application.medicare.medicare.R;

public class TakeNightLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_night_layout);
    }

    public void onBackPressed() {
        super.onBackPressed();
        //return;
    }
}

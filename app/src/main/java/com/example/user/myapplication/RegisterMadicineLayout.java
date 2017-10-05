package com.example.user.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegisterMadicineLayout extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_madicine_layout);
     spinner=(Spinner) findViewById(R.id.medicineSpinner);
        adapter=ArrayAdapter.createFromResource(this,R.array.medicine,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }
}

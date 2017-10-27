package com.example.user.medicare;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.myapplication.R;

/*
    약 추가

    TODO: 약물정보 : 약물명 / 횟수  , 하루에 몇번 ? N번 , 알람설정
*/


public class AddMedicineInfo extends AppCompatActivity {
    //약물정보
    private ArrayAdapter adapter;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine_info);

        //약물 정보 스피너 뷰
        spinner = (Spinner) findViewById(R.id.medicineSpinner);
        adapter= ArrayAdapter.createFromResource(this,R.array.medicine,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.GRAY);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });










        //알람 추가버튼
        Button alarmAddButton =(Button) findViewById(R.id.alarmAddButton);
        alarmAddButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),RegisterAlarmLayout.class);
                startActivity(intent);
            }

        });


        //등록 버튼
        Button nextPatientButton = (Button) findViewById(R.id.next_Button);
        nextPatientButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),TakeScheduleLayout.class);
                startActivity(intent);

            }
        });

        //뒤로가기
        Button backPatientButton = (Button) findViewById(R.id.back_Button);
        backPatientButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),TakeScheduleLayout.class);
                startActivity(intent);

            }
        });
    }
}


package com.example.user.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.user.myapplication.R;

public class AddMedicineActivity extends AppCompatActivity {

    //약물정보
    private ArrayAdapter adapter;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_madicine);

        //약물 정보 스피너 뷰
        spinner = (Spinner) findViewById(R.id.medicineSpinner);
        adapter= ArrayAdapter.createFromResource(this,R.array.medicine,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 뒤로가기 버튼
        Button backMedicineButton = (Button) findViewById(R.id.backMedicineButton);
        backMedicineButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent= new Intent(getApplicationContext(),AddPatientActivity.class);
                startActivity(intent);

            }
        });

        //등록버튼
        Button alarmAddButton =(Button) findViewById(R.id.alarmAddButton);
        alarmAddButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),RegisterAlarmLayout.class);
                startActivity(intent);
            }

        });

    }


}

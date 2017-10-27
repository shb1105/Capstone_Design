package com.example.user.medicare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.user.myapplication.R;

/*
    약 복용 스케줄

    TODO: 해당 환자 이름, 날짜 정보 / 약 추가버튼 / 아침 오후 점심 야간 별 약복용 스케줄 정보

*/

public class TakeScheduleLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_schedule_layout);


        //다음 버튼
        Button AddMedicineButton = (Button) findViewById(R.id.add_medicine_button);
        AddMedicineButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),AddMedicineInfo.class);
                startActivity(intent);

            }
        });


    }

}

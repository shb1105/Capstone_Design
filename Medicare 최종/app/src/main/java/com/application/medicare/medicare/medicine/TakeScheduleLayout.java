package com.application.medicare.medicare.medicine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.application.medicare.medicare.R;

/* 약 복용 스케줄 */

public class TakeScheduleLayout extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    // medicine 데이터를 로드하기 위한 식별자
    private static final int MEDICINE_LOADER = 0;

    MedicineCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        drawerLayout = findViewById(R.id.drawer_layout);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_schedule_layout);






        //버튼 별 이동

        Button MorgingButton = findViewById(R.id.morning);
        MorgingButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),TakeMorningLayout.class);
                startActivity(intent);
            }
        });


        Button LunchButton = findViewById(R.id.lunch);
        LunchButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),TakeLunchLayout.class);
                startActivity(intent);
            }
        });


        Button DinnerButton = findViewById(R.id.dinner);
        DinnerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),TakeDinnerLayout.class);
                startActivity(intent);
            }
        });

        Drawable img = getResources().getDrawable( R.drawable.ic_pills);

        LunchButton.setCompoundDrawables( img, null, null, null );


        Button NightButton = findViewById(R.id.night);
        NightButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),TakeNightLayout.class);
                startActivity(intent);
            }
        });



    }

}



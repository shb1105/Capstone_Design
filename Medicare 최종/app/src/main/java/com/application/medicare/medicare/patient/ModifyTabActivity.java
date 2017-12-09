package com.application.medicare.medicare.patient;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import com.application.medicare.medicare.R;
import com.application.medicare.medicare.medicine.TakeScheduleLayout;

/**
 * Created by johyeon-a on 2017. 11. 27..
 */

public class ModifyTabActivity extends AppCompatActivity {

    private Uri mCurrentPatientUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_tab);

        Intent intent = getIntent();
        mCurrentPatientUri = intent.getData();

//        setTitle(getString(R.string.activity_edit_patient));
        getSupportActionBar().hide();

        TabHost tabHost = findViewById(R.id.tabhost);
        LocalActivityManager local = new LocalActivityManager(this, false);
        local.dispatchCreate(savedInstanceState);
        tabHost.setup(local);

        TabHost.TabSpec addPatient = tabHost.newTabSpec("add");
        addPatient.setIndicator("환자 정보", getResources().getDrawable(R.drawable.ic_patient_info));
        //TabActivity로 온 값을 addActivity에 전달.
        Intent add = new Intent(this, AddPatientActivity.class);
        if (mCurrentPatientUri != null) {
            add.setData(mCurrentPatientUri);
        }
        addPatient.setContent(add);

        TabHost.TabSpec medicine = tabHost.newTabSpec("medicine");
        medicine.setIndicator("약 정보", getResources().getDrawable(R.drawable.ic_medicine_info));
        medicine.setContent(new Intent(this, TakeScheduleLayout.class));

        tabHost.addTab(addPatient);
        tabHost.addTab(medicine);


    }
}

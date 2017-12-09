package com.application.medicare.medicare.medicine;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.application.medicare.medicare.R;
import com.application.medicare.medicare.data.PatientContract;

/**
 * Created by Dain_Kang on 2017. 12. 1..
 */

public class MedicineCursorAdapter extends CursorAdapter {


    public MedicineCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.schedule_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);



        int nameColumnIndex = cursor.getColumnIndex(PatientContract.PatientEntry.COLUMN_PATIENT_NAME);


        String patientName = cursor.getString(nameColumnIndex);






        nameTextView.setText(patientName);


    }
}

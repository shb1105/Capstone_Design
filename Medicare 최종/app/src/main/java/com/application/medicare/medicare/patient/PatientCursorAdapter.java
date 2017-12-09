package com.application.medicare.medicare.patient;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


import com.application.medicare.medicare.R;
import com.application.medicare.medicare.data.PatientContract.PatientEntry;



public class PatientCursorAdapter extends CursorAdapter {


    public PatientCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 리스트 수정을 위한 텍스트 뷰
        TextView nameTextView = view.findViewById(R.id.name);
        TextView summaryTextView = view.findViewById(R.id.summary);


        int nameColumnIndex = cursor.getColumnIndex(PatientEntry.COLUMN_PATIENT_NAME);
        int roomNumColumnIndex = cursor.getColumnIndex(PatientEntry.COLUMN_PATIENT_ROOM_NUM);

        String patientName = cursor.getString(nameColumnIndex);
        String roomNum = cursor.getString(roomNumColumnIndex);

        if (TextUtils.isEmpty(roomNum)) {
            roomNum = context.getString(R.string.unknown_room_num);
        }



        nameTextView.setText(patientName);
        summaryTextView.setText(roomNum);
    }
}


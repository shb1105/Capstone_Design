package com.application.medicare.medicare.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.application.medicare.medicare.data.MedicineContract.MedicineEntry;
import com.application.medicare.medicare.data.PatientContract.PatientEntry;




public class PatientDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PatientDbHelper.class.getSimpleName();


    private static final String DATABASE_NAME = "medicare.db";


    private static final int DATABASE_VERSION = 1;


    public PatientDbHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }


    @Override
    public void onCreate(SQLiteDatabase db) {
        /* PATIENT TABLE */
        String SQL_CREATE_PATIENT_TABLE = "CREATE TABLE " + PatientEntry.TABLE_NAME + " ("
                + PatientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PatientEntry.COLUMN_PATIENT_NAME + " TEXT NOT NULL, "
                + PatientEntry.COLUMN_PATIENT_GENDER + " INTEGER NOT NULL, "
                + PatientEntry.COLUMN_PATIENT_ROOM_NUM + " INTEGER NOT NULL, "
                + PatientEntry.COLUMN_PATIENT_PHONE_NUM + " INTEGER NOT NULL DEFAULT 0, "
                + PatientEntry.COLUMN_PURIFIER_KEY + " TEXT );";

        /* MEDICINE TABLE */

        String SQL_CREATE_MEDICINE_TABLE = "CREATE TABLE " + MedicineEntry.TABLE_NAME + " ("
                + MedicineEntry._MEDICINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedicineEntry.COLUMN_MEDICINE_NAME + " TEXT NOT NULL , "
                + MedicineEntry.COLUMN_MEDICINE_COUNT + " INTEGER NOT NULL ,"
                + MedicineEntry.COLUMN_MEDICINE_TIME1 + " DATETIME , "
                + MedicineEntry.COLUMN_MEDICINE_TIME2 + " DATETIME , "
                + MedicineEntry.COLUMN_MEDICINE_TIME3 + " DATETIME , "
                + MedicineEntry.COLUMN_MEDICINE_TIME4 + " DATETIME , "
                + MedicineEntry.COLUMN_TAKE_START + " DATE , "
                + MedicineEntry.COLUMN_TAKE_END + " DATE );";

        /* SCHEDULE TABLE */

       /* String SQL_CREATE_SCHEDULE_TABLE = "CREATE TABLE " + ScheduleEntry.TABLE_NAME + " ("
                + ScheduleEntry._SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ScheduleEntry.COLUMN_PATIENT_ID + " INTEGER NOT NULL, "
                + ScheduleEntry.COLUMN_MEDICINE_ID + " INTEGER NOT NULL, "
                + ScheduleEntry.COLUMN_TAKE_START + " DATE NOT NULL, "
                + ScheduleEntry.COLUMN_TAKE_END + " DATE NOT NULL );"; */

        /* DETAIL SCHEDULE TABLE */

        /* String SQL_CREATE_DETAIL_SCHEDULE_TABLE = "CREATE TABLE " + DetailScheduleEntry.TABLE_NAME + " ("
                + DetailScheduleEntry._DETAIL_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DetailScheduleEntry.COLUMN_SCHEDULE_ID + " INTEGER NOT NULL, "
                + DetailScheduleEntry.COLUMN_TIME + " DATETIME NOT NULL, "
                + DetailScheduleEntry.COLUMN_TAKE_MEDICINE + " BOOLEAN NOT NULL );"; */


          db.execSQL(SQL_CREATE_PATIENT_TABLE);
          db.execSQL(SQL_CREATE_MEDICINE_TABLE);
//        db.execSQL(SQL_CREATE_SCHEDULE_TABLE);
//        db.execSQL(SQL_CREATE_DETAIL_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



    }
}
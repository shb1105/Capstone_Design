package com.example.user.medicare.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.medicare.data.MedicareContract.MedicareEntry;




public class MedicareDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MedicareDbHelper.class.getSimpleName();


    private static final String DATABASE_NAME = "medicare.db";


    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link MedicareDbHelper}.
     *
     * @param context of the app
     */
    public MedicareDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the medicare table
        String SQL_CREATE_MEDICARE_TABLE =  "CREATE TABLE " + MedicareEntry.TABLE_NAME + " ("
                + MedicareEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedicareEntry.COLUMN_MEDICARE_NAME + " TEXT NOT NULL, ";


        db.execSQL(SQL_CREATE_MEDICARE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
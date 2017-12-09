package com.application.medicare.medicare.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.application.medicare.medicare.data.PatientContract.PatientEntry;



public class PatientProvider extends ContentProvider {


    public static final String LOG_TAG = PatientProvider.class.getSimpleName();


    private static final int PATIENT = 100;


    private static final int PATIENT_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        sUriMatcher.addURI(PatientContract.CONTENT_AUTHORITY, PatientContract.PATH_PATIENT, PATIENT);


        sUriMatcher.addURI(PatientContract.CONTENT_AUTHORITY, PatientContract.PATH_PATIENT + "/#", PATIENT_ID);
    }

//    데이터베이스 헬퍼 객체
    private PatientDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new PatientDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {

            case PATIENT:

                cursor = database.query(PatientEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PATIENT_ID:

                selection = PatientEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(PatientEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PATIENT:
                return PatientEntry.CONTENT_LIST_TYPE;
            case PATIENT_ID:
                return PatientEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PATIENT:
                return insertPatient(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertPatient(Uri uri, ContentValues values) {

        String name = values.getAsString(PatientEntry.COLUMN_PATIENT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("환자 성함을 입력 해 주세요");
        }
         // 성별 체크
        Integer gender = values.getAsInteger(PatientEntry.COLUMN_PATIENT_GENDER);
        if (gender == null || !PatientEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("성별을 체크 해 주세요");
        }
        // 병실 호수 체크
        Integer RoomNum = values.getAsInteger(PatientEntry.COLUMN_PATIENT_ROOM_NUM);
        if ( RoomNum != null && RoomNum < 0) {
            throw new IllegalArgumentException("병실 호수를 입력 해 주세요");
        }
        // 보호자 번호 체크
        Integer PhoneNum = values.getAsInteger(PatientEntry.COLUMN_PATIENT_PHONE_NUM);
        if ( PhoneNum != null && PhoneNum < 0) {
            throw new IllegalArgumentException("전화번호를 입력 해 주세요.");
        }

        String PurifierKey = values.getAsString(PatientEntry.COLUMN_PURIFIER_KEY);
        if (PurifierKey == null) {
            throw new IllegalArgumentException("정수기 키 번호를 입력해 주세요.");
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        long id = database.insert(PatientEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PATIENT:
                return updatePatient(uri, contentValues, selection, selectionArgs);
            case PATIENT_ID:

                selection = PatientEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePatient(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updatePatient(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //환자 성함
        if (values.containsKey(PatientEntry.COLUMN_PATIENT_NAME)) {
            String name = values.getAsString(PatientEntry.COLUMN_PATIENT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("환자 성함을 입력 해 주세요");
            }
        }
        //성별
        if (values.containsKey(PatientEntry.COLUMN_PATIENT_GENDER)) {
            Integer gender = values.getAsInteger(PatientEntry.COLUMN_PATIENT_GENDER);
            if (gender == null || !PatientEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("성별을 체크 해 주세요,");
            }
        }
        // 병실 호수
        if (values.containsKey(PatientEntry.COLUMN_PATIENT_ROOM_NUM)) {
            Integer RoomNum = values.getAsInteger(PatientEntry.COLUMN_PATIENT_ROOM_NUM);
            if (RoomNum != null && RoomNum < 0) {
                throw new IllegalArgumentException("병실 호수를 입력해주세요");
            }
        }
        //보호자 번호
        if (values.containsKey(PatientEntry.COLUMN_PATIENT_PHONE_NUM)) {
            Integer PhoneNum = values.getAsInteger(PatientEntry.COLUMN_PATIENT_PHONE_NUM);
            if (PhoneNum != null && PhoneNum < 0) {
                throw new IllegalArgumentException("보호자 번호를 입력 해 주세요");
            }
        }
        //정수기 제품 키
        if (values.containsKey(PatientEntry.COLUMN_PURIFIER_KEY)) {
            String PurifierKey = values.getAsString(PatientEntry.COLUMN_PURIFIER_KEY);
            if (PurifierKey == null ) {
                throw new IllegalArgumentException("정수기 제품 키를 입력하세요");
            }
        }



        if (values.size() == 0) {
            return 0;
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(PatientEntry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
    // 쓰기용 데이터
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PATIENT:
                // 일치하는 모든 행, 전달인 지우기
                rowsDeleted = database.delete(PatientEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case PATIENT_ID:
                // uri 에서  ID 에 해당하는 열 삭제
                selection = PatientEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(PatientEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

}

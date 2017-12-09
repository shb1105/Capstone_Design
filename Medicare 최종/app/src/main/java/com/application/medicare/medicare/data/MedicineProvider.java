package com.application.medicare.medicare.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.application.medicare.medicare.data.MedicineContract.MedicineEntry;

/**
 * Created by Dain_Kang on 2017. 11. 5..
 */

public class MedicineProvider extends ContentProvider {

    public static final String LOG_TAG = MedicineProvider.class.getSimpleName();

    private static final int MEDICINE = 100;

    private static final int MEDICINE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(MedicineContract.CONTENT_AUTHORITY, MedicineContract.PATH_MEDICINE, MEDICINE);


        sUriMatcher.addURI(MedicineContract.CONTENT_AUTHORITY, MedicineContract.PATH_MEDICINE + "/#", MEDICINE_ID);
    }

    private PatientDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new PatientDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {

            case MEDICINE:

                cursor = database.query(MedicineContract.MedicineEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MEDICINE_ID:

                selection = MedicineContract.MedicineEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(MedicineContract.MedicineEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDICINE:
                return MedicineContract.MedicineEntry.CONTENT_LIST_TYPE;
            case MEDICINE_ID:
                return MedicineContract.MedicineEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDICINE:
                return insertMedicine(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertMedicine(Uri uri, ContentValues values) {

        //약물 이름
        Integer MedicineName = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_NAME);
        if (MedicineName == null) {
            throw new IllegalArgumentException("약 이름을 입력 해 주세요");
        }
        //복용횟수
        Integer Count = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_COUNT);
        if (Count == null) {
            throw new IllegalArgumentException("약 복용 횟수를 입력해 주세요");
        }
        //아점저야
        Integer Time1 = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_TIME1);
        if (Time1 == null) {
            throw new IllegalArgumentException("아침 복용 시간을 입력해 주세요");
        }

        Integer Time2 = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_TIME2);
        if (Time2 == null) {
            throw new IllegalArgumentException("점심 복용 시간을 입력해 주세요");
        }
        Integer Time3 = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_TIME3);
        if (Time3 == null) {
            throw new IllegalArgumentException("저녁 복용 시간을 입력해 주세요");
        }
        Integer Time4 = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_TIME4);
        if (Time4 == null) {
            throw new IllegalArgumentException("야간 복용 시간을 입력해 주세요");
        }
        //복용시작일
        Integer TakeStart = values.getAsInteger(MedicineEntry.COLUMN_TAKE_START);
        if (TakeStart == null) {
            throw new IllegalArgumentException("복용 시작일을 입력해 주세요");
        }
        //복용마감일
        Integer TakeEnd = values.getAsInteger(MedicineEntry.COLUMN_TAKE_END);
        if (TakeEnd == null) {
            throw new IllegalArgumentException("복용 마감일을 입력해 주세요");
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(MedicineEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDICINE:
                // 일치하는 모든 행, 전달인 지우기
                rowsDeleted = database.delete(MedicineContract.MedicineEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case MEDICINE_ID:
                // uri 에서  ID 에 해당하는 열 삭제
                selection = MedicineContract.MedicineEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(MedicineContract.MedicineEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDICINE:
                return updateMedicine(uri, contentValues, selection, selectionArgs);
            case MEDICINE_ID:

                selection = MedicineContract.MedicineEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMedicine(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateMedicine(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        //약물명
        if (values.containsKey(MedicineEntry.COLUMN_MEDICINE_NAME)) {
            Integer medicine = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_NAME);
            if (medicine == null || !MedicineEntry.isValidMedicine(medicine)) {
                throw new IllegalArgumentException("약물 명을 체크 해 주세요,");
            }
        }
        // 하루몇변
        if (values.containsKey(MedicineEntry.COLUMN_MEDICINE_COUNT)) {
            Integer MedicineCount = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_COUNT);
            if (MedicineCount != null && MedicineCount < 0) {
                throw new IllegalArgumentException(" 복용 횟수를 입력해주세요");
            }
        }
        // 아침 점심 저녁 야간
        if (values.containsKey(MedicineEntry.COLUMN_MEDICINE_TIME1)) {
            Integer Time1 = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_TIME1);
            if (Time1 != null && Time1 < 0) {
                throw new IllegalArgumentException("아침시간 입력 해 주세요");
            }
        }

        if (values.containsKey(MedicineEntry.COLUMN_MEDICINE_TIME1)) {
            Integer Time2 = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_TIME2);
            if (Time2 != null && Time2 < 0) {
                throw new IllegalArgumentException("점심시간 입력 해 주세요");
            }
        }

        if (values.containsKey(MedicineEntry.COLUMN_MEDICINE_TIME1)) {
            Integer Time3 = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_TIME3);
            if (Time3 != null && Time3 < 0) {
                throw new IllegalArgumentException("저녁시간 입력 해 주세요");
            }
        }

        if (values.containsKey(MedicineEntry.COLUMN_MEDICINE_TIME1)) {
            Integer Time4 = values.getAsInteger(MedicineEntry.COLUMN_MEDICINE_TIME4);
            if (Time4 != null && Time4 < 0) {
                throw new IllegalArgumentException("야간 시간 입력 해 주세요");
            }
        }
        // 복용 시작일
        if (values.containsKey(MedicineEntry.COLUMN_TAKE_START)) {
            Integer TakeStart = values.getAsInteger(MedicineEntry.COLUMN_TAKE_START);
            if (TakeStart == null ) {
                throw new IllegalArgumentException("복용시작일 입력하세요");
            }
        }

        if (values.containsKey(MedicineEntry.COLUMN_TAKE_END)) {
            Integer TakeEnd = values.getAsInteger(MedicineEntry.COLUMN_TAKE_END);
            if (TakeEnd == null ) {
                throw new IllegalArgumentException("복용마감일를 입력하세요");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(MedicineContract.MedicineEntry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}

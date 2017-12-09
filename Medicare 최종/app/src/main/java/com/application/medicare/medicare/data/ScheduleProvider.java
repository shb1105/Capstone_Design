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

/**
 * Created by Dain_Kang on 2017. 11. 5..
 */

public class ScheduleProvider extends ContentProvider {

    public static final String LOG_TAG = ScheduleProvider.class.getSimpleName();

    private static final int SCHEDULE = 100;

    private static final int SCHEDULE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(ScheduleContract.CONTENT_AUTHORITY, ScheduleContract.PATH_SCHEDULE, SCHEDULE);


        sUriMatcher.addURI(ScheduleContract.CONTENT_AUTHORITY, ScheduleContract.PATH_SCHEDULE + "/#", SCHEDULE_ID);
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

            case SCHEDULE:

                cursor = database.query(ScheduleContract.ScheduleEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SCHEDULE_ID:

                selection = ScheduleContract.ScheduleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(ScheduleContract.ScheduleEntry.TABLE_NAME, projection, selection, selectionArgs,
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
            case SCHEDULE:
                return ScheduleContract.ScheduleEntry.CONTENT_LIST_TYPE;
            case SCHEDULE_ID:
                return ScheduleContract.ScheduleEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SCHEDULE:
                return insertSchedule(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertSchedule(Uri uri, ContentValues values) {

        Integer PatientId = values.getAsInteger(ScheduleContract.ScheduleEntry.COLUMN_PATIENT_ID);
        if ( PatientId != null && PatientId < 0) {
            throw new IllegalArgumentException("환자 아이디가 필요 합니다");
        }

        Integer MedicineId = values.getAsInteger(ScheduleContract.ScheduleEntry.COLUMN_MEDICINE_ID);
        if ( MedicineId != null && MedicineId < 0) {
            throw new IllegalArgumentException("약 아이디가 필요 합니다");
        }

        Integer TakeStart = values.getAsInteger(ScheduleContract.ScheduleEntry.COLUMN_TAKE_START);
        if ( TakeStart != null && TakeStart < 0) {
            throw new IllegalArgumentException("복용 시작일이 필요 합니다");
        }

        Integer TakeEnd = values.getAsInteger(ScheduleContract.ScheduleEntry.COLUMN_TAKE_END);
        if ( TakeEnd != null && TakeEnd < 0) {
            throw new IllegalArgumentException("복용 완료일이 필요 합니다");
        }





        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ScheduleContract.ScheduleEntry.TABLE_NAME, null, values);

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
            case SCHEDULE:
                // 일치하는 모든 행, 전달인 지우기
                rowsDeleted = database.delete(ScheduleContract.ScheduleEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case SCHEDULE_ID:
                // uri 에서  ID 에 해당하는 열 삭제
                selection = ScheduleContract.ScheduleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ScheduleContract.ScheduleEntry.TABLE_NAME, selection, selectionArgs);
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
            case SCHEDULE:
                return updateSchedule(uri, contentValues, selection, selectionArgs);
            case SCHEDULE_ID:

                selection = MedicineContract.MedicineEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateSchedule(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateSchedule(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ScheduleContract.ScheduleEntry.COLUMN_PATIENT_ID)) {
            Integer PatientId = values.getAsInteger(ScheduleContract.ScheduleEntry.COLUMN_PATIENT_ID);
            if (PatientId != null && PatientId < 0) {
                throw new IllegalArgumentException("환자 아이디가 필요합니다 ");
            }
        }

        if (values.containsKey(ScheduleContract.ScheduleEntry.COLUMN_MEDICINE_ID)) {
            Integer MedicineId = values.getAsInteger(ScheduleContract.ScheduleEntry.COLUMN_MEDICINE_ID);
            if (MedicineId != null && MedicineId < 0) {
                throw new IllegalArgumentException("약 아이디가 필요합니다 ");
            }
        }

        if (values.containsKey(ScheduleContract.ScheduleEntry.COLUMN_TAKE_START)) {
            Integer TakeStart = values.getAsInteger(ScheduleContract.ScheduleEntry.COLUMN_TAKE_START);
            if (TakeStart != null && TakeStart < 0) {
                throw new IllegalArgumentException("복용 시작일이 필요합니다 ");
            }
        }

        if (values.containsKey(ScheduleContract.ScheduleEntry.COLUMN_TAKE_END)) {
            Integer TakeEnd = values.getAsInteger(ScheduleContract.ScheduleEntry.COLUMN_TAKE_END);
            if (TakeEnd != null && TakeEnd < 0) {
                throw new IllegalArgumentException("복용 완료일이 필요합니다 ");
            }
        }

        if (values.size() == 0) {
            return 0;
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(ScheduleContract.ScheduleEntry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }
}

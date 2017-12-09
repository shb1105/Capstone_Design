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

public class DetailScheduleProvider extends ContentProvider {

    public static final String LOG_TAG = DetailScheduleProvider.class.getSimpleName();

    private static final int DETAIL_SCHEDULE = 100;

    private static final int DETAIL_SCHEDULE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(DetailScheduleContract.CONTENT_AUTHORITY, DetailScheduleContract.PATH_DETAIL_SCHEDULE, DETAIL_SCHEDULE);


        sUriMatcher.addURI(DetailScheduleContract.CONTENT_AUTHORITY, DetailScheduleContract.PATH_DETAIL_SCHEDULE + "/#", DETAIL_SCHEDULE_ID);
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

            case DETAIL_SCHEDULE:

                cursor = database.query(DetailScheduleContract.DetailScheduleEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case DETAIL_SCHEDULE_ID:

                selection = DetailScheduleContract.DetailScheduleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(DetailScheduleContract.DetailScheduleEntry.TABLE_NAME, projection, selection, selectionArgs,
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
            case DETAIL_SCHEDULE:
                return DetailScheduleContract.DetailScheduleEntry.CONTENT_LIST_TYPE;
            case DETAIL_SCHEDULE_ID:
                return DetailScheduleContract.DetailScheduleEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DETAIL_SCHEDULE:
                return insertDetailSchedule(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertDetailSchedule(Uri uri, ContentValues values) {

        Integer ScheduleId = values.getAsInteger(DetailScheduleContract.DetailScheduleEntry.COLUMN_SCHEDULE_ID);
        if ( ScheduleId != null && ScheduleId < 0) {
            throw new IllegalArgumentException("스케줄 아이디가 필요 합니다");
        }

        Integer time = values.getAsInteger(DetailScheduleContract.DetailScheduleEntry.COLUMN_TIME);
        if ( time != null && time < 0) {
            throw new IllegalArgumentException("복용시간이 필요 합니다");
        }

        Boolean TakeMedicine = values.getAsBoolean(DetailScheduleContract.DetailScheduleEntry.COLUMN_TAKE_MEDICINE);
        if ( TakeMedicine == null ) {
            throw new IllegalArgumentException("복용여부가 필요 합니다");
        }



        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(DetailScheduleContract.DetailScheduleEntry.TABLE_NAME, null, values);

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
            case DETAIL_SCHEDULE:
                // 일치하는 모든 행, 전달인 지우기
                rowsDeleted = database.delete(DetailScheduleContract.DetailScheduleEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case DETAIL_SCHEDULE_ID:
                // uri 에서  ID 에 해당하는 열 삭제
                selection = DetailScheduleContract.DetailScheduleEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(DetailScheduleContract.DetailScheduleEntry.TABLE_NAME, selection, selectionArgs);
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
            case DETAIL_SCHEDULE:
                return updateDetailSchedule(uri, contentValues, selection, selectionArgs);
            case DETAIL_SCHEDULE_ID:

                selection = MedicineContract.MedicineEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateDetailSchedule(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateDetailSchedule(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(DetailScheduleContract.DetailScheduleEntry.COLUMN_SCHEDULE_ID)) {
            Integer ScheduleId = values.getAsInteger(DetailScheduleContract.DetailScheduleEntry.COLUMN_SCHEDULE_ID);
            if (ScheduleId != null && ScheduleId < 0) {
                throw new IllegalArgumentException("스케줄 아이디가 필요합니다 ");
            }
        }

        if (values.containsKey(DetailScheduleContract.DetailScheduleEntry.COLUMN_TIME)) {
            Integer time = values.getAsInteger(DetailScheduleContract.DetailScheduleEntry.COLUMN_TIME);
            if (time != null && time < 0) {
                throw new IllegalArgumentException("복용시간이 필요합니다 ");
            }
        }

        if (values.containsKey(DetailScheduleContract.DetailScheduleEntry.COLUMN_TAKE_MEDICINE)) {
            Boolean TakeMedicine = values.getAsBoolean(DetailScheduleContract.DetailScheduleEntry.COLUMN_TAKE_MEDICINE);
            if (TakeMedicine == null) {
                throw new IllegalArgumentException("복용여부가 필요합니다 ");
            }
        }

        if (values.size() == 0) {
            return 0;
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(DetailScheduleContract.DetailScheduleEntry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }


}

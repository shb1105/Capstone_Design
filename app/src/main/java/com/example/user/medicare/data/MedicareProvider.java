package com.example.user.medicare.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.user.medicare.data.MedicareContract.MedicareEntry;



public class MedicareProvider extends ContentProvider {


    public static final String LOG_TAG = MedicareProvider.class.getSimpleName();


    private static final int Medicare = 100;


    private static final int MEDICARE_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        sUriMatcher.addURI(MedicareContract.CONTENT_AUTHORITY, MedicareContract.PATH_Medicare, Medicare);


        sUriMatcher.addURI(MedicareContract.CONTENT_AUTHORITY, MedicareContract.PATH_Medicare + "/#", MEDICARE_ID);
    }

    private MedicareDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MedicareDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();


        Cursor cursor;


        int match = sUriMatcher.match(uri);
        switch (match) {
            case Medicare:

                cursor = database.query(MedicareEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MEDICARE_ID:

                selection = MedicareEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(MedicareEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Medicare:
                return insertMedicare(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertMedicare(Uri uri, ContentValues values) {

        String name = values.getAsString(MedicareEntry.COLUMN_MEDICARE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Medicare requires a name");
        }




        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        long id = database.insert(MedicareEntry.TABLE_NAME, null, values);

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
            case Medicare:
                return updateMedicare(uri, contentValues, selection, selectionArgs);
            case MEDICARE_ID:

                selection = MedicareEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMedicare(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateMedicare(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(MedicareEntry.COLUMN_MEDICARE_NAME)) {
            String name = values.getAsString(MedicareEntry.COLUMN_MEDICARE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Medicare requires a name");
            }
        }



        if (values.size() == 0) {
            return 0;
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(MedicareEntry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Medicare:

                rowsDeleted = database.delete(MedicareEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEDICARE_ID:

                selection = MedicareEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(MedicareEntry.TABLE_NAME, selection, selectionArgs);
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
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Medicare:
                return MedicareEntry.CONTENT_LIST_TYPE;
            case MEDICARE_ID:
                return MedicareEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}

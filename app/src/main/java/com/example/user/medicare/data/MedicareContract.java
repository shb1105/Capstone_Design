package com.example.user.medicare.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dain_Kang on 2017. 10. 10..
 */

public final class MedicareContract {

    private MedicareContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.user.application";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_Medicare = "medicare";

    public static final class MedicareEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_Medicare);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Medicare;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Medicare;


        public final static String TABLE_NAME = "medicare";


        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_MEDICARE_NAME ="name";




    }
}

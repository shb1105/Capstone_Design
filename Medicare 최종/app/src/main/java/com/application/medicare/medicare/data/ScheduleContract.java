package com.application.medicare.medicare.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dain_Kang on 2017. 11. 5..
 */

public final class ScheduleContract {

    private ScheduleContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.user.medicare";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SCHEDULE = "schedule";

    public static final class ScheduleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SCHEDULE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE;

        public final static String TABLE_NAME = "schedule";

        public final static String _SCHEDULE_ID = BaseColumns._ID;

        public final static String COLUMN_PATIENT_ID = "patientId";

        public final static String COLUMN_MEDICINE_ID = "medicineId";

        public final static String COLUMN_TAKE_START = "takeStart";

        public final static String COLUMN_TAKE_END = "takeEnd";


    }
}

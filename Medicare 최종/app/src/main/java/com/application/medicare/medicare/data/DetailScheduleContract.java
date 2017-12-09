package com.application.medicare.medicare.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dain_Kang on 2017. 11. 5..
 */

public final class DetailScheduleContract {
    private  DetailScheduleContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.user.medicare";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DETAIL_SCHEDULE = "DetailSchedule";

    public static final class DetailScheduleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DETAIL_SCHEDULE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAIL_SCHEDULE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAIL_SCHEDULE;

        /* 상세 스케줄 테이블 */
        public final static String TABLE_NAME = "DetailSchedule";

        /* 상세 스케줄 ID */
        public final static String _DETAIL_SCHEDULE_ID = BaseColumns._ID;

        /* 스케줄 ID */
        public final static String COLUMN_SCHEDULE_ID = "ScheduleId";

        /* 복용시간 */
        public final static String COLUMN_TIME = "time";

        /* 복용여부 */
        public final static String COLUMN_TAKE_MEDICINE = "TakeMedicine";
    }
}

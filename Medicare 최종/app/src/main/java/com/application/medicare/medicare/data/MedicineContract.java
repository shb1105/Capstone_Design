package com.application.medicare.medicare.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dain_Kang on 2017. 11. 5..
 */

public final class MedicineContract {
    private MedicineContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.user.medicare";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MEDICINE = "medicine";

    public static final class MedicineEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEDICINE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICINE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEDICINE;
        /* 약 테이블 */
        public final static String TABLE_NAME = "medicine";
        /* 약 아이디 */
        public final static String _MEDICINE_ID = BaseColumns._ID;
        // 약 이름
        public final static String COLUMN_MEDICINE_NAME ="medicineName";
        //약 횟수
        public final static String COLUMN_MEDICINE_COUNT="medicineCount";
        //약 시간
        public final static String COLUMN_MEDICINE_TIME1="medicineTime1";
        public final static String COLUMN_MEDICINE_TIME2="medicineTime2";
        public final static String COLUMN_MEDICINE_TIME3="medicineTime3";
        public final static String COLUMN_MEDICINE_TIME4="medicineTime4";

        //약복용일
        public final static String COLUMN_TAKE_START="TakeStart";
        public final static String COLUMN_TAKE_END="TakeEnd";

//       타이레놀,항균제,감염제,소아약,심혈제,혈압약
        public static final int MEDICINE1 = 0;
        public static final int MEDICINE2 = 1;
        public static final int MEDICINE3 = 2;
        public static final int MEDICINE4 = 3;
        public static final int MEDICINE5 = 4;
        public static final int MEDICINE6 = 5;

        public static boolean isValidMedicine(int medicine) {
            if ( medicine == MEDICINE1 || medicine == MEDICINE2 || medicine == MEDICINE3
                    || medicine == MEDICINE4 || medicine == MEDICINE5 || medicine == MEDICINE6 ) {

                return true;
            }
            return false;
        }

    }

}

package com.application.medicare.medicare.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dain_Kang on 2017. 10. 10..
 */

public final class PatientContract {

    private PatientContract() {}

    public static final String CONTENT_AUTHORITY = "com.application.medicare.medicare";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PATIENT = "patient";

    public static final class PatientEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PATIENT);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATIENT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PATIENT;


        public final static String TABLE_NAME = "patient";


        public final static String _ID = BaseColumns._ID;

        /* 환자성함 */
        public final static String COLUMN_PATIENT_NAME = "name";
        /* 성별 */
        public final static String COLUMN_PATIENT_GENDER = "gender";
        /* 호실 */
        public final static String COLUMN_PATIENT_ROOM_NUM = "RoomNum";
        /* 보호자 연락처 */
        public final static String COLUMN_PATIENT_PHONE_NUM = "PhoneNum";
        /* 정수기 키 번호 */
        public final static String COLUMN_PURIFIER_KEY = "PurifierKey";

        public static final int GENDER_MALE = 0;
        public static final int GENDER_FEMALE = 1;

        public static boolean isValidGender(int gender) {
            if ( gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }
    }

}

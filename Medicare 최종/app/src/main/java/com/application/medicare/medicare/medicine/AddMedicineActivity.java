package com.application.medicare.medicare.medicine;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.application.medicare.medicare.HttpClient;
import com.application.medicare.medicare.R;
import com.application.medicare.medicare.data.MedicineContract;
import com.application.medicare.medicare.patient.PatientListActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/* 약 추가
* Log.v("AddPatientActivity", "EDIT_TEXT_NAME:" + name );
* */


public class AddMedicineActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    //약물정보
    private ArrayAdapter adapter;
    private Spinner spinner;

    //환자정보
    private String  patientName;

    //약물 정보 변경 사항
    private boolean mMedicineHasChanged = false;

    /* 존재하는 환자 데이터를 로드 하기 위한 식별자  */
    private static final int EXISTING_MEDICINE_LOADER = 0;

    private Uri mCurrentMedicineUri;

    /* 약물 명 스피너 */
    private Spinner mMedicineSpinner;

    /*  하루에 몇번 */
    private EditText mCountEditText;

    /*  아침 점심 저녁 야간 */
    private Button TakeMorningButton;
    private Button TakeLunchButton;
    private Button TakeDinnerButton;
    private Button TakeNightButton;

    /* 약 복용 시작일 수정 필드 */
    private EditText mTakeStartEditText;

    /* 약 복용 종료일 수정 필드 */
    private EditText mTakeEndEditText;

    private int mMedicine = MedicineContract.MedicineEntry.MEDICINE1;

    //알람 timepicker button
    Button timeBtn1,timeBtn2,timeBtn3,timeBtn4;

    String date[] = new String[4];


    // 기존 환자리스트 수정을 위한 터치 이벤트
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressWarnings("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mMedicineHasChanged = false;
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);


        Intent intent = getIntent();
        mCurrentMedicineUri = intent.getData();



        mMedicineSpinner = findViewById(R.id.medicine_spinner);
        mCountEditText =  findViewById(R.id.take_in_a_day);
        TakeMorningButton =  findViewById(R.id.alarmAddButton_1);
        TakeLunchButton =  findViewById(R.id.alarmAddButton_2);
        TakeDinnerButton =  findViewById(R.id.alarmAddButton_3);
        TakeNightButton =  findViewById(R.id.alarmAddButton_4);
        mTakeStartEditText = findViewById(R.id.take_start);
        mTakeEndEditText = findViewById(R.id.take_end);

        mMedicineSpinner.setOnTouchListener( mTouchListener);
        mCountEditText.setOnTouchListener( mTouchListener);
        TakeMorningButton.setOnTouchListener( mTouchListener);
        TakeLunchButton.setOnTouchListener( mTouchListener);
        TakeDinnerButton.setOnTouchListener( mTouchListener);
        TakeNightButton.setOnTouchListener( mTouchListener);
        mTakeEndEditText.setOnTouchListener( mTouchListener);
        mTakeStartEditText.setOnTouchListener( mTouchListener);


        //알람 추가 버튼과 timepicker 연결
        timeBtn1 = (Button) findViewById(R.id.alarmAddButton_1);
        timeBtn2 = (Button) findViewById(R.id.alarmAddButton_2);
        timeBtn3 = (Button) findViewById(R.id.alarmAddButton_3);
        timeBtn4 = (Button) findViewById(R.id.alarmAddButton_4);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);




        // 환자 추가 클래스로 부터 입력한 환자 이름 받아오기
        Intent idIntent = getIntent();
        patientName = idIntent.getExtras().getString("name");
        TextView patient = findViewById(R.id.patient_name);
        patient.setText(patientName);

        setupSpinner();

    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.medicine, android.R.layout.simple_spinner_dropdown_item);


        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        mMedicineSpinner.setAdapter(genderSpinnerAdapter);



        mMedicineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    //       타이레놀,항균제,감염제,소아약,심혈제,혈압약
                    if (selection.equals("타이레놀")) {
                        mMedicine = MedicineContract.MedicineEntry.MEDICINE1;
                    }else if(selection.equals("향균제")){
                        mMedicine = MedicineContract.MedicineEntry.MEDICINE2;

                    }else if(selection.equals("감염제")){
                        mMedicine = MedicineContract.MedicineEntry.MEDICINE3;

                    }else if(selection.equals("소아약")){
                        mMedicine = MedicineContract.MedicineEntry.MEDICINE4;

                    }else if(selection.equals("심혈제")){
                        mMedicine = MedicineContract.MedicineEntry.MEDICINE5;

                    }else {
                        mMedicine = MedicineContract.MedicineEntry.MEDICINE6;
                    }

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mMedicine = MedicineContract.MedicineEntry.MEDICINE1;
            }
        });
    }

    //timepickerDialog 알람 설정
    public void setTime(final View view){

        Calendar calendar = Calendar.getInstance();
        int hour= calendar.get(Calendar.HOUR);
        final int minute= calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(
                AddMedicineActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker tView, int hourOfDay, int minute) {

                String hour, min;
                if (hourOfDay < 10) {
                    hour = "0"+hourOfDay;
                } else {
                    hour = String.valueOf(hourOfDay);
                }
                if (minute < 10) {
                    min = "0"+minute;
                } else {
                    min = String.valueOf(minute);
                }

                switch (view.getId()) {
                    case R.id.alarmAddButton_1:
                        timeBtn1.setText(hourOfDay+ " 시 "+minute + " 분에 알람이 설정되었습니다." );
                        date[0] = hour+":"+min;
                        break;
                    case R.id.alarmAddButton_2:
                        timeBtn2.setText(hourOfDay+ " 시 "+minute + " 분에 알람이 설정되었습니다." );
                        date[1] = hour+":"+min;
                        break;
                    case R.id.alarmAddButton_3:
                        timeBtn3.setText(hourOfDay+ " 시 "+minute + " 분에 알람이 설정되었습니다." );
                        date[2] = hour+":"+min;
                        break;
                    case R.id.alarmAddButton_4:
                        timeBtn4.setText(hourOfDay+ " 시 "+minute + " 분에 알람이 설정되었습니다." );
                        date[3] = hour+":"+min;
                        break;
                }

            }
        },hour,minute,true);

        timePickerDialog.show();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_medicine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 등록 버튼을 누를 경우 데이터 베이스 저장
            case R.id.action_save:
//                saveMedicine();

                medicineNetwork network = new medicineNetwork();
                Map<String,String>params = new HashMap<>();
                params.put("patient",patientName);

                params.put("time1", date[0]);
                params.put("time2", date[1]);
                params.put("time3", date[2]);
                params.put("time4", date[3]);


                network.execute(params);

                // 약 추가 화면으로 이동
                //Toast.makeText(this, date[0]+"/"+date[1]+"/"+date[2]+"/"+date[3], Toast.LENGTH_SHORT).show();

                Intent medicineIntent = new Intent(this, PatientListActivity.class);
                startActivity(medicineIntent);
                finish();
                return true;
            case android.R.id.home:

                if (!mMedicineHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddMedicineActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(AddMedicineActivity.this);
                            }
                        };

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (!mMedicineHasChanged) {
            super.onBackPressed();
            return;
        }


    }





    private void saveMedicine() {

        String CountString = mCountEditText.getText().toString().trim();
        String TakeMorningString = TakeMorningButton.getText().toString().trim();
        String TakeLunchString = TakeLunchButton.getText().toString().trim();
        String TakeDinnerString = TakeDinnerButton.getText().toString().trim();
        String TakeNightString = TakeNightButton.getText().toString().trim();
        String TakeStartString = mTakeStartEditText.getText().toString().trim();
        String TakeEndString = mTakeEndEditText.getText().toString().trim();

        // 새로운 환자가 추가 되는지 확인
        // 빈 작성 목록이 있는지 확인
        if (mCurrentMedicineUri == null &&
                TextUtils.isEmpty(CountString) && mMedicine ==  MedicineContract.MedicineEntry.MEDICINE1 &&
                TextUtils.isEmpty(TakeMorningString) && TextUtils.isEmpty(TakeLunchString) && TextUtils.isEmpty(TakeDinnerString) && TextUtils.isEmpty(TakeNightString) &&
                TextUtils.isEmpty(TakeStartString) &&TextUtils.isEmpty(TakeEndString)) {

            return;
        }





        //해당 열에 키값 생성

        ContentValues values = new ContentValues();
        values.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME, mMedicine);
        values.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_COUNT, CountString);
        values.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME1, date[0]);
        values.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME2, date[1]);
        values.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME3, date[2]);
        values.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME4, date[3]);
        values.put(MedicineContract.MedicineEntry.COLUMN_TAKE_START, TakeStartString);
        values.put(MedicineContract.MedicineEntry.COLUMN_TAKE_END, TakeEndString);




//        if (mCurrentMedicineUri == null) {
//            //존재하지 않는 환자 일 경우 provider 에 추가
//            Uri newUri = getContentResolver().insert(MedicineContract.MedicineEntry.CONTENT_URI, values);
//
//            if (newUri == null) {
//                // If the new content URI is null, then there was an error with insertion.
//                Toast.makeText(this, getString(R.string.insert_patient_failed),
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, getString(R.string.insert_patient_successful),
//                        Toast.LENGTH_SHORT).show();
//            }
//        }

        //존개하는 환자인지 아닌지 확인
        if (mCurrentMedicineUri == null) {
            //존재하지 않는 환자 일 경우 provider 에 추가
            Uri newUri = getContentResolver().insert(MedicineContract.MedicineEntry.CONTENT_URI, values);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "약 추가 실패",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "약 추가 성공",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //약 아이디 , 약물이름 , 하루 몇번 ? ,  아침점심저녁야
        String[] projection = {
                MedicineContract.MedicineEntry._ID,
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME,
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_COUNT,
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME1,
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME2,
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME3,
                MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME4,
                MedicineContract.MedicineEntry.COLUMN_TAKE_START,
                MedicineContract.MedicineEntry.COLUMN_TAKE_END };


        return new CursorLoader(this,
                mCurrentMedicineUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if(cursor.moveToFirst()) {

            int MedicineColumnIndex = cursor.getColumnIndex(MedicineContract.MedicineEntry.COLUMN_MEDICINE_NAME);
            int CountColumnIndex = cursor.getColumnIndex(MedicineContract.MedicineEntry.COLUMN_MEDICINE_COUNT);
            int Time1ColumnIndex = cursor.getColumnIndex(MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME1);
            int Time2ColumnIndex = cursor.getColumnIndex(MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME2);
            int Time3ColumnIndex = cursor.getColumnIndex(MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME3);
            int Time4ColumnIndex = cursor.getColumnIndex(MedicineContract.MedicineEntry.COLUMN_MEDICINE_TIME4);
            int TakeStartColumnIndex = cursor.getColumnIndex(MedicineContract.MedicineEntry.COLUMN_TAKE_START);
            int TakeEndColumnIndex = cursor.getColumnIndex(MedicineContract.MedicineEntry.COLUMN_TAKE_END);

            int medicine = cursor.getInt(MedicineColumnIndex);
            int Count = cursor.getInt(CountColumnIndex);
            int time1 = cursor.getInt(Time1ColumnIndex);
            int time2 = cursor.getInt(Time2ColumnIndex);
            int time3 = cursor.getInt(Time3ColumnIndex);
            int time4 = cursor.getInt(Time4ColumnIndex);
            int TakeStart = cursor.getInt(TakeStartColumnIndex);
            int TakeEnd = cursor.getInt(TakeEndColumnIndex);


            mCountEditText.setText(Integer.toString(Count));
            TakeMorningButton.setText(Integer.toString(time1));
            TakeLunchButton.setText(Integer.toString(time2));
            TakeDinnerButton.setText(Integer.toString(time3));
            TakeNightButton.setText(Integer.toString(time4));
            mTakeStartEditText.setText(Integer.toString(TakeStart));
            mTakeEndEditText.setText(Integer.toString(TakeEnd));

            switch (medicine) {

                case MedicineContract.MedicineEntry.MEDICINE2:
                    mMedicineSpinner.setSelection(1);
                    break;

                case MedicineContract.MedicineEntry.MEDICINE3:
                    mMedicineSpinner.setSelection(2);
                    break;

                case MedicineContract.MedicineEntry.MEDICINE4:
                    mMedicineSpinner.setSelection(4);
                    break;

                case MedicineContract.MedicineEntry.MEDICINE5:
                    mMedicineSpinner.setSelection(5);
                    break;

                default:
                    mMedicineSpinner.setSelection(0);
                    break;
            }



        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMedicineSpinner.setSelection(0); // 기본 타이레놀
        mCountEditText.setText("");
        TakeMorningButton.setText("");
        TakeLunchButton.setText("");
        TakeDinnerButton.setText("");
        TakeNightButton.setText("");
        mTakeStartEditText.setText("");
        mTakeEndEditText.setText("");
    }



    public class medicineNetwork extends AsyncTask<Map<String,String>, Integer,String> {
        /** * doInBackground 실행되기 이전에 동작한다. */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String,String>...maps){
            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.43.151:8080/test/setAlarm3");

            //파라미터 전송
            //http.addOrReplace("test","테스트입니다.");

            http.addAllParameters(maps[0]);
            //http.addAllParameters(params);


            // HTTP 요청 전송
            HttpClient post = http.create(); post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }
        /** * doInBackground 종료되면 동작한다. * @param s : doInBackground가 리턴한 값이 들어온다. */

        @Override
        protected void onPostExecute(String s) {


        }
    }
}

package com.application.medicare.medicare.patient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.application.medicare.medicare.R;
import com.application.medicare.medicare.data.PatientContract;
import com.application.medicare.medicare.data.PatientContract.PatientEntry;
import com.application.medicare.medicare.medicine.AddMedicineActivity;


// TODO: 환자 추가 : 1. 환자를 추가하면 환자 리스트에 해당 환자 추가 2. 기존 환자 리스트 수정


public class AddPatientActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /* 존재하는 환자 데이터를 로드 하기 위한 식별자  */
    private static final int EXISTING_PATIENT_LOADER = 0;

    private Uri mCurrentPatientUri;

    /*  환자 성함  수정 필드  */
    private EditText mNameEditText;

    /* 환자 성별 텍스트 수정 필드  */
    private Spinner mGenderSpinner;

    /*  환자 병실 호수 텍스트 수정 필드 */
    private EditText mRoomNumEditText;

    /*  보호자 번호 텍스트 수정 필드 */
    private EditText mFamilyPhoneNumEditText;

    /* 환자 정수기 제품 키 텍스트 수정 필드 */
    private EditText mPurifierKeyEditText;


    private int mGender = PatientEntry.GENDER_MALE;

    private boolean mPatientHasChanged = false;

    // 기존 환자리스트 수정을 위한 터치 이벤트
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressWarnings("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPatientHasChanged = true;
            return false;
        }
    };


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        //약정보 탭으로 이동
//                ImageButton nextPatientButton = findViewById(R.id.imageButton2);
//                nextPatientButton.setOnClickListener(new View.OnClickListener(){
//
//                    @Override
//            public void onClick(View v){
//
//                        Intent intent=new Intent(getApplicationContext(),TakeScheduleLayout.class);
//                        startActivity(intent);
//                    }
//        });



        // 환자를 추가 하는지 or 기존 환자 수정
        Intent intent = getIntent();
        mCurrentPatientUri = intent.getData();

        if (mCurrentPatientUri == null) {
            //Appbar 환자추가
            setTitle(getString(R.string.activity_new_patient));


        } else {
            // 환자 리스트가 존재 하면 Appbar 환자 수정
            setTitle(getString(R.string.activity_edit_patient));
//            getSupportActionBar().hide();
//            View tabButton = (View) findViewById(R.id.tabButton);
//            tabButton.setVisibility(View.GONE);

            // 데이터베이스 로더 초기화
            getLoaderManager().initLoader(EXISTING_PATIENT_LOADER, null, this);
        }


        mNameEditText = (EditText) findViewById(R.id.patient_name);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mRoomNumEditText = (EditText) findViewById(R.id.patient_room_num);
        mFamilyPhoneNumEditText = (EditText) findViewById(R.id.patient_phone_num);
        mPurifierKeyEditText = (EditText) findViewById(R.id.purifier_key);

        mNameEditText.setOnTouchListener( mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);
        mRoomNumEditText.setOnTouchListener(mTouchListener);
        mFamilyPhoneNumEditText.setOnTouchListener(mTouchListener);
        mPurifierKeyEditText.setOnTouchListener(mTouchListener);



        setupSpinner();

    }




    private void savePatient() {

        String NameString = mNameEditText.getText().toString().trim();
        String RoomNumString = mRoomNumEditText.getText().toString().trim();
        String phoneString = mFamilyPhoneNumEditText.getText().toString().trim();
        String PurifierKeyString = mPurifierKeyEditText.getText().toString().trim();

        // 새로운 환자가 추가 되는지 확인
        // 빈 작성 목록이 있는지 확인
        if (mCurrentPatientUri == null &&
                TextUtils.isEmpty(NameString) && mGender == PatientContract.PatientEntry.GENDER_MALE
                && TextUtils.isEmpty(RoomNumString) && TextUtils.isEmpty(phoneString) && TextUtils.isEmpty(PurifierKeyString) ) {

            return;
        }

        //해당 열에 키값 생성

        ContentValues values = new ContentValues();
        values.put(PatientContract.PatientEntry.COLUMN_PATIENT_NAME, NameString);
        values.put(PatientContract.PatientEntry.COLUMN_PATIENT_GENDER, mGender);
        values.put(PatientContract.PatientEntry.COLUMN_PATIENT_ROOM_NUM, RoomNumString);
        values.put(PatientContract.PatientEntry.COLUMN_PURIFIER_KEY, PurifierKeyString);

        // 유저가 보호자 번호를 입력 하지 않은 경우
        // 기본값으로 0
        int family_phone_number = 0;
        if (!TextUtils.isEmpty(phoneString)) {
            family_phone_number = Integer.parseInt(phoneString);
        }
        values.put(PatientContract.PatientEntry.COLUMN_PATIENT_PHONE_NUM, family_phone_number);
        //존개하는 환자인지 아닌지 확인
        if (mCurrentPatientUri == null) {
            //존재하지 않는 환자 일 경우 provider 에 추가
            Uri newUri = getContentResolver().insert(PatientContract.PatientEntry.CONTENT_URI, values);

            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.insert_patient_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_patient_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // 환자가 존재 하는 경우. 업데이트
            // 환자 정보를 수정
            int rowsAffected = getContentResolver().update(mCurrentPatientUri, values, null, null);

            if (rowsAffected == 0) {
                //열이 바뀌지 않았을 경우
                Toast.makeText(this, getString(R.string.update_patient_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_patient_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);


        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        mGenderSpinner.setAdapter(genderSpinnerAdapter);


        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PatientEntry.GENDER_MALE;
                    } else {
                        mGender = PatientEntry.GENDER_FEMALE;
                    }

                }
            }
            //  성별 선택 안했을 경우 기본 남성으로 표시
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PatientContract.PatientEntry.GENDER_MALE;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_patient, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // 새로 환자를 추가 하는 경우 제거 버튼 숨기기
        if (mCurrentPatientUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }else {
            // 이미 추가된 환자 리스트를 확인 하는 경우 버튼 숨기기
            MenuItem menuItem = menu.findItem(R.id.action_save);
            menuItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 등록 버튼을 누를 경우 데이터 베이스 저장
            case R.id.action_save:
                savePatient();
                // 약 추가 화면에 입력한 환자 이름 보내기
                String name = mNameEditText.getText().toString().trim();
                Log.v("AddPatientActivity", "EDIT_TEXT_NAME:" + name );
                if((mCurrentPatientUri == null)) {
                    // 약 추가 화면으로 이동
                    Intent medicineIntent = new Intent(this, AddMedicineActivity.class);
                    medicineIntent.putExtra("name", name);

                    startActivity(medicineIntent);
                }else{
                    finish();
                }
//                finish();
                return true;
            // Delete 버튼 클릭시
            case R.id.action_delete:
                // Delete 팝업 창 보이기
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:

                if (!mPatientHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddPatientActivity.this);
                    return true;
                }


                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(AddPatientActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mPatientHasChanged) {
            super.onBackPressed();
            return;
        }

        //저장 되지 않은 변경사항에 대한 알림 메세지
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                PatientEntry._ID,
                PatientEntry.COLUMN_PATIENT_NAME,
                PatientEntry.COLUMN_PATIENT_GENDER,
                PatientEntry.COLUMN_PATIENT_ROOM_NUM,
                PatientEntry.COLUMN_PATIENT_PHONE_NUM,
                PatientEntry.COLUMN_PURIFIER_KEY };


        return new CursorLoader(this,
                mCurrentPatientUri,
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


        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(PatientEntry.COLUMN_PATIENT_NAME);
            int genderColumnIndex = cursor.getColumnIndex(PatientEntry.COLUMN_PATIENT_GENDER);
            int RoomNumColumnIndex = cursor.getColumnIndex(PatientEntry.COLUMN_PATIENT_ROOM_NUM);
            int PhoneNumColumnIndex = cursor.getColumnIndex(PatientEntry.COLUMN_PATIENT_PHONE_NUM);
            int PurifierKeyColumnIndex = cursor.getColumnIndex(PatientEntry.COLUMN_PURIFIER_KEY);

            String name = cursor.getString(nameColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int RoomNum = cursor.getInt(RoomNumColumnIndex);
            int PhoneNum = cursor.getInt(PhoneNumColumnIndex);
            String PurifierKey = cursor.getString(PurifierKeyColumnIndex);

            mNameEditText.setText(name);
            mRoomNumEditText.setText(Integer.toString(RoomNum));
            mFamilyPhoneNumEditText.setText(Integer.toString(PhoneNum));
            mPurifierKeyEditText.setText(PurifierKey);


            switch (gender) {
                case PatientEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(1);
                    break;

                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mGenderSpinner.setSelection(0); // 기본 남성 선택
        mRoomNumEditText.setText("");
        mFamilyPhoneNumEditText.setText("");
        mPurifierKeyEditText.setText("");

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // 취소를 누를 경우 계속 수정
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // delete 버튼을 누를 경우 제거
                deletePatient();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // 취소 버튼을 누를 경우 계속 환자 수정화면에 머무르기
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // 알림창 생성 및 보이기
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePatient() {

        if (mCurrentPatientUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentPatientUri, null, null);


            if (rowsDeleted == 0) {

                Toast.makeText(this, getString(R.string.delete_patient_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.delete_patient_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }


        finish();
    }


}
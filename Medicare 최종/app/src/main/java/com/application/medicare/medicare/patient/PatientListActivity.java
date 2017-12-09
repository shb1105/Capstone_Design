package com.application.medicare.medicare.patient;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.application.medicare.medicare.R;
import com.application.medicare.medicare.data.PatientContract.PatientEntry;
import com.google.firebase.messaging.FirebaseMessaging;

public class PatientListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    public DrawerLayout drawerLayout;
     // patient 데이터를 로드하기 위한 식별자
    private static final int PATIENT_LOADER = 0;


     // 환자 리스트뷰 어뎁터
    PatientCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        FirebaseMessaging.getInstance().subscribeToTopic("ALL");

        // 환자 추가 FAB  버튼
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        PatientListActivity.this, AddPatientActivity.class);
                startActivity(intent);
            }
        });

    //환자 리스트 뷰
    ListView patientListView = findViewById(R.id.patient_list);


    // 리스트에 환자 목록이 없을때
    View emptyView = findViewById(R.id.empty_view);
    patientListView.setEmptyView(emptyView);
    //환자 리스트 추가 어댑터 생성
    mCursorAdapter = new PatientCursorAdapter(this, null);
    patientListView.setAdapter(mCursorAdapter);


    patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick (AdapterView < ? > adapterView, View view,int position, long id ){
            // 환자 리스트 클릭 시 해당 id 리스트 보여주기
//            Intent intent = new Intent(PatientListActivity.this, AddPatientActivity.class);
            Intent intent = new Intent(PatientListActivity.this, ModifyTabActivity.class);
            Uri currentPatientUri = ContentUris.withAppendedId(PatientEntry.CONTENT_URI, id);

            intent.setData(currentPatientUri);
            startActivity(intent);


            }

        });

        getLoaderManager().initLoader(PATIENT_LOADER, null, this);
    }

    // 데이터 베이스 환자 리스트 모두 제거
    private void deleteAllPatient() {
        int rowsDeleted = getContentResolver().delete(PatientEntry.CONTENT_URI, null, null);
        Log.v("PatientActivity", rowsDeleted + " rows deleted from patient database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_patient_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            deleteAllPatient();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

                String[] projection = {
                        PatientEntry._ID,
                        PatientEntry.COLUMN_PATIENT_NAME,
                        PatientEntry.COLUMN_PATIENT_ROOM_NUM
                };


        return new CursorLoader(this,
                PatientEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }

        @Override
        public void onLoadFinished (Loader < Cursor > loader, Cursor data){

            mCursorAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {

            mCursorAdapter.swapCursor(null);
        }
    }


package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.w3c.dom.Text;

public class AddPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);


        //다음 버튼 이동
        Button nextPatientButton = (Button) findViewById(R.id.nextPatientButton);
        nextPatientButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),AddMadicineActivity.class);
                startActivity(intent);

            }
        });


        Button backPatientButton = (Button) findViewById(R.id.backPatientButton);
        backPatientButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });
    }//onCreate


    //데이터베이스
/*
   public class PatientInfoHelper extends SQLiteOpenHelper{

       final static String TA6 = "PatientInfoHelper";

       public PatientInfoHelper(Context c){

           super(c,"NameInfoHelper.db",null,1);
       }


       @Override
       public void onCreate(SQLiteDatabase sqLiteDatabase) {
           Log.i(TA6,"onCreate()");
           String query="CREATE TABLE PatientInfoHelper(_id INTEGER PRIMARY KEY AUTOINCREMENT, pName char(20), pNumber INTEGER);";
           db.execSQL(query);
       }

       @Override
       public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

           Log.i(TA6,"onUpgrade()");
           String query ="DROP TABLE IF EXITS patient";
           db.execSQL(query);
           onCreate(db);

       }
   }

    public class
*/
}

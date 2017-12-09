package com.application.medicare.medicare;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {
    private TextView idText;
    private TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText =(EditText) findViewById(R.id.passwordText);

        Button joinButton = (Button) findViewById(R.id.joinButton);

        joinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userId=  idText.getText().toString();//String문자열로 변환
                String userPassword = passwordText.getText().toString();

                JoinNetwork network = new JoinNetwork();
                Map<String,String> params = new HashMap<>();
                params.put("userId",userId);
                params.put("userPassword",userPassword);
                network.execute(params);


            }
        });

    }

    public void onBackPressed() {
            super.onBackPressed();
            //return;
        }

    public class JoinNetwork extends AsyncTask<Map<String,String>, Integer,String> {
        /** * doInBackground 실행되기 이전에 동작한다. */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String,String>...maps){
            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.43.151:8080/test/join");

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

            // Log.d("JSON_RESULT", s);

            Gson gson = new Gson();
            Data data = gson.fromJson(s,Data.class);

            if(data.getData1().equals("success")){
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this); //현재창에서 성공 알람창 띄우기
                builder.setMessage("회원 등록 성공했습니다.")
                        .setPositiveButton("확인",null)
                        .create()
                        .show();

                Intent intent = new Intent(JoinActivity.this , LoginActivity.class);
                JoinActivity.this.startActivity(intent);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this); //현재창에서 성공 알람창 띄우기
                builder.setMessage("회원에 등록 실패했습니다.")
                        .setNegativeButton("확인",null)
                        .create()
                        .show();

            }


        }
    }

}


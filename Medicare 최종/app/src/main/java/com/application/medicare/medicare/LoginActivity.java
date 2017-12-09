package com.application.medicare.medicare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.medicare.medicare.patient.PatientListActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final EditText id, pwd;
        String loginId, loginPwd;
        Button btn;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        loginId = auto.getString("inputId",null);
        loginPwd = auto.getString("inputPwd",null);
        btn = (Button)findViewById(R.id.loginButton);
        id = (EditText)findViewById(R.id.idText);
        pwd = (EditText)findViewById(R.id.passwordText);


        if(loginId !=null && loginPwd != null) {
            if(loginId.equals("medi") && loginPwd.equals("1111")) {
                Toast.makeText(LoginActivity.this, loginId +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,PatientListActivity.class);
                startActivity(intent);
                finish();
            }
        }

        else if(loginId == null && loginPwd == null){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (id.getText().toString().equals("medi") && pwd.getText().toString().equals("1111")) {
                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        //아이디가 '부르곰'이고 비밀번호가 '네이버'일 경우 SharedPreferences.Editor를 통해
                        //auto의 loginId와 loginPwd에 값을 저장해 줍니다.
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.putString("inputId", id.getText().toString());
                        autoLogin.putString("inputPwd", pwd.getText().toString());
                        //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                        autoLogin.commit();
                        Toast.makeText(LoginActivity.this, id.getText().toString()+"님 환영합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, PatientListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }




        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        TextView joinButton =(TextView) findViewById(R.id.joinButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent joinIntent = new Intent(LoginActivity.this, JoinActivity.class);
                LoginActivity.this.startActivity(joinIntent);

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userId = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

                LoginNetwork network = new LoginNetwork();
                Map<String,String> params = new HashMap<>();
                params.put("userId",userId);
                params.put("userPassword",userPassword);

                network.execute(params);

            }
        });

    }

    public class LoginNetwork extends AsyncTask<Map<String,String>, Integer,String> {
        /** * doInBackground 실행되기 이전에 동작한다. */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String,String>...maps){
            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.43.151:8080/test/login");

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

                //AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                Intent intent = new Intent(LoginActivity.this , PatientListActivity.class);
                intent.putExtra("userId",data.getUserId());
                intent.putExtra("userPassword",data.getUserPassword());
                LoginActivity.this.startActivity(intent); //메인화면으로 값 넘겨주기

            }else if(data.getData1().equals("fail")){
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this); //현재창에서 성공 알람창 띄우기
                builder.setMessage("로그인에 실패 했습니다.")
                        .setNegativeButton("다시시도",null)
                        .create()
                        .show();

            }


        }
    }

}
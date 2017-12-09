package com.application.medicare.medicare;

/**
 * Created by user1 on 2017-11-07.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    Data data = new Data();
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);
        data.setToken(token);
        // 생성등록된 토큰을 개인 앱서버에 보내 저장해 두었다가 추가 뭔가를 하고 싶으면 할 수 있도록 한다.
        sendRegistrationToServer(token);

    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // Add custom implementation, as needed.
/*
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .build();


        //request
        Request request = new Request.Builder()
                .url("http://172.17.0.102:8080/test/sendGcm")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TokenNetwork network = new TokenNetwork();
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
        network.execute(params);
*/
    }

    public class TokenNetwork extends AsyncTask<Map<String,String>,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Map<String,String>...maps){
            // HTTP 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://192.168.25.44:8080/test/android");

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

package com.application.medicare.medicare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by johyeon-a on 2017. 12. 1..
 */

public class loadingclass extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loadingclass);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
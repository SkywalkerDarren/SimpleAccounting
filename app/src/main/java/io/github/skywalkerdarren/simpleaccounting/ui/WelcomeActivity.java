package io.github.skywalkerdarren.simpleaccounting.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import io.github.skywalkerdarren.simpleaccounting.R;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(() -> {
            Intent intent = MainActivity.newIntent(WelcomeActivity.this);
            startActivity(intent);
            WelcomeActivity.this.finish();
        }, 2000);
    }
}

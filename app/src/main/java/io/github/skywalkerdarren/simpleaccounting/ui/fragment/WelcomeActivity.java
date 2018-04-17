package io.github.skywalkerdarren.simpleaccounting.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.MainActivity;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        new Handler().postDelayed(() -> {
            Intent intent = MainActivity.newIntent(WelcomeActivity.this);
            startActivity(intent);
            WelcomeActivity.this.finish();
        }, 2000);
    }
}

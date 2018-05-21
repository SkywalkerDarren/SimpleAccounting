package io.github.skywalkerdarren.simpleaccounting.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import io.github.skywalkerdarren.simpleaccounting.base.BaseFragmentActivity;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.MyAccountFragment;

public class MyAccountActivity extends BaseFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, MyAccountActivity.class);
    }

    @Override
    public Fragment createFragment() {
        return MyAccountFragment.newInstance();
    }
}

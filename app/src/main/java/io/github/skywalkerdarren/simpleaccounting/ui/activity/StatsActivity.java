package io.github.skywalkerdarren.simpleaccounting.ui.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import io.github.skywalkerdarren.simpleaccounting.base.BaseFragmentActivity;
import io.github.skywalkerdarren.simpleaccounting.ui.fragment.StatsFragment;

public class StatsActivity extends BaseFragmentActivity {
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, StatsActivity.class);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return StatsFragment.newInstance();
    }
}

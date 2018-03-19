package io.github.skywalkerdarren.simpleaccounting.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;

/**
 * 编辑账单
 *
 * @author darren
 * @date 2018/3/8
 */

public class BillEditActivity extends BaseFragmentActivity {
    private static final String EXTRA_BILL = "bill";
    public static final String EXTRA_CENTER_X = "io.github.skywalkerdarren.simpleaccounting.centerX";
    public static final String EXTRA_CENTER_Y = "io.github.skywalkerdarren.simpleaccounting.centerY";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public static Intent newIntent(Context context, Bill bill, int x, int y) {
        Intent intent = new Intent(context, BillEditActivity.class);
        intent.putExtra(EXTRA_BILL, bill);
        intent.putExtra(EXTRA_CENTER_X, x);
        intent.putExtra(EXTRA_CENTER_Y, y);
        return intent;
    }


    @Override
    public void onBackPressed() {
        NumPad numPad = findViewById(R.id.num_key_view);
        if (numPad.getVisibility() == View.VISIBLE) {
            numPad.hideKeyboard();
            return;
        }
        supportFinishAfterTransition();
    }

    @Override
    public Fragment createFragment() {
        Bill bill = (Bill) getIntent().getSerializableExtra(EXTRA_BILL);
        int cx = getIntent().getIntExtra(EXTRA_CENTER_X, 0);
        int cy = getIntent().getIntExtra(EXTRA_CENTER_Y, 0);
        return BillEditFragment.newInstance(bill, cx, cy);
    }
}

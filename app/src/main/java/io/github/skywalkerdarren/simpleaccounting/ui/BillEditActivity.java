package io.github.skywalkerdarren.simpleaccounting.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Explode;
import android.transition.Fade;

import io.github.skywalkerdarren.simpleaccounting.model.Bill;

/**
 * Created by darren on 2018/3/8.
 */

public class BillEditActivity extends BaseFragmentActivity {
    private static final String EXTRA_BILL = "bill";
    public static final String EXTRA_TRANS = "io.github.skywalkerdarren.simpleaccounting.transition";
    public static final String SLIDE_UP = "slide up";
    public static final String CIRCLE_UP = "circle up";
    private Bill mBill;

    public static Intent newIntent(Context context, Bill bill) {
        Intent intent = new Intent(context, BillEditActivity.class);
        intent.putExtra(EXTRA_BILL, bill);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        mBill = (Bill) getIntent().getSerializableExtra(EXTRA_BILL);

        switch (getIntent().getStringExtra(EXTRA_TRANS)) {
            case SLIDE_UP:
                getWindow().setEnterTransition(new Explode());
                getWindow().setExitTransition(new Explode());
                break;
            case CIRCLE_UP:
                getWindow().setEnterTransition(new Fade());
                getWindow().setExitTransition(new Fade());
                break;
            default:
                break;
        }
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public Fragment createFragment() {
        mBill = (Bill) getIntent().getSerializableExtra(EXTRA_BILL);
        return BillEditFragment.newInstance(mBill);
    }
}

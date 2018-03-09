package io.github.skywalkerdarren.simpleaccounting.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import io.github.skywalkerdarren.simpleaccounting.model.Bill;

/**
 * Created by darren on 2018/3/8.
 */

public class BillEditActivity extends BaseFragmentActivity {
    private static final String EXTRA_BILL = "bill";
    private Bill mBill;

    public static Intent newIntent(Context context, Bill bill) {
        Intent intent = new Intent(context, BillEditActivity.class);
        intent.putExtra(EXTRA_BILL, bill);
        return intent;
    }

    @Override
    public void setOnCreate() {
        mBill = (Bill) getIntent().getSerializableExtra(EXTRA_BILL);
    }

    @Override
    public Fragment createFragment() {
        return BillEditFragment.newInstance(mBill);
    }
}

package io.github.skywalkerdarren.simpleaccounting.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import io.github.skywalkerdarren.simpleaccounting.model.Bill;

public class BillDetailPagerActivity extends BaseFragmentActivity {
    private static final String EXTRA_BILL = "bill";
    private Bill mBill;

    public static Intent newIntent(Context context, Bill bill) {
        Intent intent = new Intent(context, BillDetailPagerActivity.class);
        intent.putExtra(EXTRA_BILL, bill);
        return intent;
    }


    @Override
    public Fragment createFragment() {
        mBill = (Bill) getIntent().getSerializableExtra(EXTRA_BILL);
        return BillDetailFragment.newInstance(mBill);
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

}

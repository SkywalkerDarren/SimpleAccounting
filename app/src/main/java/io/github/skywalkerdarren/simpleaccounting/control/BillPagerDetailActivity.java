package io.github.skywalkerdarren.simpleaccounting.control;

import android.support.v4.app.Fragment;

public class BillPagerDetailActivity extends BaseFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new BillDetailFragment();
    }

}

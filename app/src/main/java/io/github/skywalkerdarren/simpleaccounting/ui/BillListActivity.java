package io.github.skywalkerdarren.simpleaccounting.ui;

import android.support.v4.app.Fragment;

public class BillListActivity extends BaseFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new BillListFragment();
    }
}

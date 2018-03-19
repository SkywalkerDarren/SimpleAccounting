package io.github.skywalkerdarren.simpleaccounting.ui;

import android.support.v4.app.Fragment;

/**
 * 账单列表的活动
 */
public class BillListActivity extends BaseFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new BillListFragment();
    }
}
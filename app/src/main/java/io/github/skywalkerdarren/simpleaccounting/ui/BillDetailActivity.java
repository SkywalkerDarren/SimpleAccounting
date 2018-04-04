package io.github.skywalkerdarren.simpleaccounting.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.view.View;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;

/**
 * 账单细节
 *
 * @author darren
 * @date 2018/2/21
 */
public class BillDetailActivity extends BaseFragmentActivity {
    private static final String EXTRA_BILL = "bill";
    public static final String EXTRA_CENTER_X = "io.github.skywalkerdarren.simpleaccounting.centerX";
    public static final String EXTRA_CENTER_Y = "io.github.skywalkerdarren.simpleaccounting.centerY";
    public static final String EXTRA_START_COLOR = "io.github.skywalkerdarren.simpleaccounting.startColor";

    /**
     * @param context 应用上下文
     * @param bill    账单
     * @param x       点击位置x
     * @param y       点击位置y
     * @param color   初始颜色
     * @return 意图
     */
    public static Intent newIntent(Context context, Bill bill, int x, int y, @ColorRes int color) {
        Intent intent = new Intent(context, BillDetailActivity.class);
        intent.putExtra(EXTRA_BILL, bill);
        intent.putExtra(EXTRA_START_COLOR, color);
        intent.putExtra(EXTRA_CENTER_X, x);
        intent.putExtra(EXTRA_CENTER_Y, y);
        return intent;
    }


    @Override
    public Fragment createFragment() {
        Bill bill = (Bill) getIntent().getSerializableExtra(EXTRA_BILL);
        View v = findViewById(R.id.fragment_container);
        int w = v.getWidth() / 2;
        int h = v.getHeight() / 2;
        int cx = getIntent().getIntExtra(EXTRA_CENTER_X, w);
        int cy = getIntent().getIntExtra(EXTRA_CENTER_Y, h);
        int startColor = getIntent().getIntExtra(EXTRA_START_COLOR, R.color.colorPrimary);
        return BillDetailFragment.newInstance(bill, cx, cy, startColor);
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

}

package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseMultiItemDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillHeaderBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillWithoutRemarkBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepositry;
import io.github.skywalkerdarren.simpleaccounting.model.entity.BillInfo;
import io.github.skywalkerdarren.simpleaccounting.ui.activity.BillDetailActivity;
import io.github.skywalkerdarren.simpleaccounting.util.AppExecutors;

/**
 * 账单适配器
 * 将帐单列表适配到recycler view
 *
 * @author darren
 * @date 2018/2/12
 */

public class BillAdapter extends BaseMultiItemDataBindingAdapter<BillInfo, ViewDataBinding> {

    private final AppRepositry mRepositry = AppRepositry.getInstance(new AppExecutors(), mContext);
    private int mX, mY;

    /**
     * 分隔符
     */
    public static final int HEADER = 2;
    /**
     * 不带备注的账单
     */
    public static final int WITHOUT_REMARK = 0;
    /**
     * 带备注的账单
     */
    public static final int WITH_REMARK = 1;
    private Activity mActivity;

    /**
     * 将帐单列表适配到适配器中
     *
     * @param bills 含分隔符的账单信息列表
     */
    public BillAdapter(List<BillInfo> bills, Activity activity) {
        super(bills);
        mActivity = activity;
        addItemType(WITH_REMARK, R.layout.item_list_bill);
        addItemType(WITHOUT_REMARK, R.layout.item_list_bill_without_remark);
        addItemType(HEADER, R.layout.item_list_bill_header);
    }

    /**
     * 设置新帐单
     *
     * @param bills 含分隔符的账单信息列表
     */
    public void setBills(List<BillInfo> bills) {
        setNewData(bills);
    }

    @Override
    protected void convert(ViewDataBinding binding, BillInfo item) {
        if (binding instanceof ItemListBillBinding) {
            ItemListBillBinding itemListBillBinding = ((ItemListBillBinding) binding);
            itemListBillBinding.setBillInfo(item);
            ImageView imageView = itemListBillBinding.typeImageView;
            itemListBillBinding.contentCardView.setOnClickListener(v -> click(item, imageView));
            itemListBillBinding.contentCardView.setOnTouchListener((v, event) -> {
                touch(event);
                return false;
            });

        } else if (binding instanceof ItemListBillWithoutRemarkBinding) {
            ItemListBillWithoutRemarkBinding itemListBillWithoutRemarkBinding = (ItemListBillWithoutRemarkBinding) binding;
            itemListBillWithoutRemarkBinding.setBillInfo(item);
            ImageView imageView = itemListBillWithoutRemarkBinding.typeImageView;
            itemListBillWithoutRemarkBinding.contentCardView.setOnClickListener(v -> click(item, imageView));
            itemListBillWithoutRemarkBinding.contentCardView.setOnTouchListener((v, event) -> {
                touch(event);
                return false;
            });
        } else if (binding instanceof ItemListBillHeaderBinding) {
            ((ItemListBillHeaderBinding) binding).setBillInfo(item);
        }
    }

    private void touch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mX = (int) event.getRawX();
            mY = (int) event.getRawY();
        }
    }

    private void click(BillInfo item, ImageView imageView) {
        mRepositry.getBill(item.getUUID(), bill -> {
            Intent intent = BillDetailActivity.newIntent(mContext,
                    bill, mX, mY, R.color.orangea200);
            intent.putExtra(BillDetailActivity.EXTRA_START_COLOR, R.color.orangea200);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    mActivity, imageView, "type_image_view");
            mContext.startActivity(intent, options.toBundle());
        });
    }
}

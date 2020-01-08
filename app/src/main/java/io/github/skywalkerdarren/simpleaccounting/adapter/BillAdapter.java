package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.content.Intent;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.adapter.diff.BillInfoDiff;
import io.github.skywalkerdarren.simpleaccounting.base.BaseMultiItemDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillHeaderBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillWithoutRemarkBinding;
import io.github.skywalkerdarren.simpleaccounting.model.AppRepository;
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
    private final AppRepository mRepository;
    private int mX, mY;
    private FragmentActivity mActivity;

    /**
     * 将帐单列表适配到适配器中
     *
     * @param bills 含分隔符的账单信息列表
     */
    public BillAdapter(List<BillInfo> bills, FragmentActivity activity) {
        super(bills);
        mActivity = activity;
        mRepository = AppRepository.getInstance(new AppExecutors(), activity);
        addItemType(WITH_REMARK, R.layout.item_list_bill);
        addItemType(WITHOUT_REMARK, R.layout.item_list_bill_without_remark);
        addItemType(HEADER, R.layout.item_list_bill_header);
    }

    public void setNewList(@Nullable List<BillInfo> data) {
        setNewDiffData(new BillInfoDiff(data));
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
        mRepository.getBill(item.getUuid(), bill -> {
            Intent intent = BillDetailActivity.newIntent(mContext,
                    bill, mX, mY, R.color.orangea200);
            intent.putExtra(BillDetailActivity.EXTRA_START_COLOR, R.color.orangea200);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    mActivity, imageView, "type_image_view");
            mContext.startActivity(intent, options.toBundle());
        });
    }
}

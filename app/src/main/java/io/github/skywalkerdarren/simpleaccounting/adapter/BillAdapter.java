package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;

import java.util.List;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.base.BaseMultiItemDataBindingAdapter;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillHeaderBinding;
import io.github.skywalkerdarren.simpleaccounting.databinding.ItemListBillWithoutRemarkBinding;
import io.github.skywalkerdarren.simpleaccounting.view_model.BillInfoViewModel;

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
    static final int WITHOUT_REMARK = 0;
    /**
     * 带备注的账单
     */
    static final int WITH_REMARK = 1;
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
        BillInfoViewModel viewModel = new BillInfoViewModel(item, mActivity);
        if (binding instanceof ItemListBillBinding) {
            ((ItemListBillBinding) binding).setBill(viewModel);
            viewModel.setImagePair(((ItemListBillBinding) binding).typeImageView);
        } else if (binding instanceof ItemListBillWithoutRemarkBinding) {
            ((ItemListBillWithoutRemarkBinding) binding).setBill(viewModel);
            viewModel.setImagePair(((ItemListBillWithoutRemarkBinding) binding).typeImageView);
        } else if (binding instanceof ItemListBillHeaderBinding) {
            ((ItemListBillHeaderBinding) binding).setHeader(viewModel);
        }
    }
}
